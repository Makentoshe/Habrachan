package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.api.MobileLoginApi
import com.makentoshe.habrachan.network.request.MobileLoginRequest
import com.makentoshe.habrachan.network.response.LoginResponse2
import okhttp3.*
import retrofit2.Retrofit
import java.util.regex.Pattern

class MobileLoginManager private constructor(
    private val api: MobileLoginApi,
    private val client: OkHttpClient,
    private val cookieJar: CustomCookieJar
) : LoginManager2<MobileLoginRequest> {

    override fun request(userSession: UserSession, email: String, password: String): MobileLoginRequest {
        return MobileLoginRequest(userSession, email, password)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun login(request: MobileLoginRequest) : Result<LoginResponse2> {
        // get cookies. consumer, state and referer
        val cookieResponse = api.getLoginCookies().execute()
        val referer = cookieResponse.raw().request.url
        val state = referer.queryParameter("state") ?: ""
        val consumer = referer.queryParameter("consumer") ?: ""

        // get window.location.href redirect
        val loginResponse = api.login(referer.toString(), request.email, request.password, state, consumer).execute()
        val loginResponseString = loginResponse.body()?.string() ?: ""

        // parse window.location.href = 'url_here' string
        val url = getUrlsFromString(loginResponseString).first()

        // redirect to get more login cookies such as phpsessid and others
        val redirectResponse = client.newCall(Request.Builder().url(url).build()).execute()

        val mobileResponse = LoginResponse2.MobileResponse(cookieJar.cookies.values.flatten())
        return Result.success(LoginResponse2(request, mobileResponse = mobileResponse))
    }

    private fun getUrlsFromString(string: String): List<String> {
        // Pattern for recognizing a URL, based off RFC 3986
        val urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)" + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*" + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL
        )

        val urls = ArrayList<String>()
        val matcher = urlPattern.matcher(string)
        while (matcher.find()) {
            urls.add(string.substring(matcher.start(1), matcher.end()))
        }

        return urls
    }

    class Builder(client: OkHttpClient) {

        private val baseUrl = "https://account.habr.com"
        private val cookieJar = CustomCookieJar()
        private val client = client.newBuilder().cookieJar(cookieJar).build()

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = MobileLoginManager(getRetrofit().create(MobileLoginApi::class.java), client, cookieJar)
    }

    private class CustomCookieJar : CookieJar {

        val cookies = HashMap<HttpUrl, ArrayList<Cookie>>()

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            return cookies.values.flatten()
        }

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            this.cookies[url] = (this.cookies[url] ?: ArrayList()).apply {
                addAll(cookies)
            }
        }
    }
}