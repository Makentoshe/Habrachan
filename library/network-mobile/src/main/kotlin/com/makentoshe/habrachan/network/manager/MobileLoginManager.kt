package com.makentoshe.habrachan.network.manager

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.makentoshe.habrachan.entity.user
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.api.MobileLoginApi
import com.makentoshe.habrachan.network.api.MobileUsersApi
import com.makentoshe.habrachan.network.deserializer.MobileGetUserDeserializer
import com.makentoshe.habrachan.network.deserializer.MobileLoginDeserializer
import com.makentoshe.habrachan.network.fold
import com.makentoshe.habrachan.network.request.MobileGetUserRequest
import com.makentoshe.habrachan.network.request.MobileLoginRequest
import com.makentoshe.habrachan.network.response.LoginResponse
import okhttp3.*
import org.jsoup.Jsoup
import retrofit2.Retrofit
import java.lang.IllegalStateException
import java.util.regex.Pattern

class MobileLoginManager private constructor(
    private val loginApi: MobileLoginApi,
    private val client: OkHttpClient,
    private val cookieJar: CustomCookieJar,
    private val deserializer: MobileLoginDeserializer,
    private val userManager: MobileGetUserManager?
) : LoginManager<MobileLoginRequest> {

    override fun request(userSession: UserSession, email: String, password: String): MobileLoginRequest {
        return MobileLoginRequest(userSession, email, password)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun login(request: MobileLoginRequest): Result<LoginResponse> {
        // get cookies. consumer, state and referer
        val cookieResponse = loginApi.getLoginCookies().execute()
        val referer = cookieResponse.raw().request.url
        val state = referer.queryParameter("state") ?: ""
        val consumer = referer.queryParameter("consumer") ?: ""

        // get window.location.href redirect
        val loginResponse =
            loginApi.login(referer.toString(), request.email, request.password, state, consumer).execute()
        val loginResponseString = loginResponse.body()?.string() ?: ""

        // parse window.location.href = 'url_here' string
        val url = getUrlsFromString(loginResponseString).firstOrNull()
            ?: return deserializer.error(request, loginResponseString)

        // redirect to get more login cookies such as phpsessid and others
        // TODO(low) we can gain and sync some settings or current habr states from response
        val redirectResponse = client.newCall(Request.Builder().url(url).build()).execute()

        val mobileResponse = LoginResponse.MobileResponse(cookieJar.cookies.values.flatten())

        if (userManager != null) {
            val string = redirectResponse.body?.string() ?: return Result.failure(IllegalStateException())
            val login = parseRedirectResponse(string)
            val userRequest = userManager.request(request.userSession, login)
            return userManager.user(userRequest).fold({
                Result.success(LoginResponse(request, mobileResponse = mobileResponse, user = it.user))
            }, {
                // TODO specify exception: Login was ok but user was failed
                Result.failure(it)
            })
        } else {
            return Result.success(LoginResponse(request, mobileResponse = mobileResponse, user = null))
        }
    }

    private fun parseRedirectResponse(string: String): String {
        val json = Jsoup.parse(string).select("body > script").firstOrNull()?.data()?.jsons()?.firstOrNull()
        val me = Gson().fromJson(json, Map::class.java)["me"]//["user"]//["alias"].toString()
        val user = (me as LinkedTreeMap<String, Any>)["user"]
        val alias = (user as LinkedTreeMap<String, Any>)["alias"]
        return alias.toString()
    }

    private fun String.jsons(): List<String> {
        val stack: MutableList<Char> = ArrayList()
        val jsons: MutableList<String> = ArrayList()
        var temp = ""
        for (eachChar in toCharArray()) {
            if (stack.isEmpty() && eachChar == '{') {
                stack.add(eachChar)
                temp += eachChar
            } else if (stack.isNotEmpty()) {
                temp += eachChar
                if (stack[stack.size - 1] == '{' && eachChar == '}') {
                    stack.removeAt(stack.size - 1)
                    if (stack.isEmpty()) {
                        jsons.add(temp)
                        temp = ""
                    }
                } else if (eachChar == '{' || eachChar == '}') stack.add(eachChar)
            } else if (temp.isNotEmpty() && stack.isEmpty()) {
                jsons.add(temp)
                temp = ""
            }
        }
        return jsons
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

    /**
     * pass user manager for second request that allows to return user in login response
     * if userManager == null so user is also will be null
     */
    class Builder(client: OkHttpClient, private val deserializer: MobileLoginDeserializer, private val userManager: MobileGetUserManager? = null) {

        private val baseUrl = "https://account.habr.com"
        private val cookieJar = CustomCookieJar()
        private val client = client.newBuilder().cookieJar(cookieJar).build()

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() =
            MobileLoginManager(getRetrofit().create(MobileLoginApi::class.java), client, cookieJar, deserializer, userManager)
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