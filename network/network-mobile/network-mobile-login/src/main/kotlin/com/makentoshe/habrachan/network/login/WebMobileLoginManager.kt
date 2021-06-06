package com.makentoshe.habrachan.network.login

import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.functional.fold
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.api.MobileLoginApi
import com.makentoshe.habrachan.network.fold
import okhttp3.*
import retrofit2.Retrofit

/**
 * This manager uses an external web tool for logging.
 *
 * Should use with ui, that works like a web browser, e.g. WebView.
 */
class WebMobileLoginManager internal constructor(
    private val loginApi: MobileLoginApi,
    private val client: OkHttpClient,
    private val cookieJar: CustomCookieJar,
    private val deserializer: WebMobileLoginDeserializer
) {

    fun request(userSession: UserSession, external: suspend (String) -> String): WebMobileLoginRequest {
        return WebMobileLoginRequest(userSession, external)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun login(request: WebMobileLoginRequest): Result<WebMobileLoginResponse> {
        try {
            // get cookies. consumer, state and referer
            val cookieResponse = loginApi.getLoginCookies().execute()
            val referer = cookieResponse.raw().request.url
            val state = referer.queryParameter("state") ?: ""
            val consumer = referer.queryParameter("consumer") ?: ""
            val loginScreenUrl =
                "https://account.habr.com/login/?state=$state&consumer=$consumer&hl=${request.userSession.habrLanguage}"

            // pass a login screen and optional google recaptcha using external tool
            val loginScreenPassedUrl = request.external(loginScreenUrl)
            val loginScreenPassedRequest = Request.Builder().url(loginScreenPassedUrl).build()
            val loginScreenPassedResponse = client.newCall(loginScreenPassedRequest).execute()

            // parse an external tool response
            return loginScreenPassedResponse.fold({
                val string = it.string()
                deserializer.deserializeLoginScreenPassedResponse(string).fold({ state ->
                    val cookies = cookieJar.cookies.flatMap { it.value }
                    Result.success(WebMobileLoginResponse(request, cookies, string, state))
                }, {
                    Result.failure(WebMobileLoginException(request, it))
                })
            }, {
                Result.failure(WebMobileLoginException(request, Exception(it.string())))
            })
        } catch (exception: Exception) {
            return Result.failure(WebMobileLoginException(request, exception))
        }
    }

    class Builder(client: OkHttpClient) {

        private val webMobileLoginDeserializer = WebMobileLoginDeserializer()
        private val cookieJar = CustomCookieJar()
        private val baseUrl = "https://account.habr.com"
        private val client = client.newBuilder().cookieJar(cookieJar).build()

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = WebMobileLoginManager(
            getRetrofit().create(MobileLoginApi::class.java),
            client,
            cookieJar,
            webMobileLoginDeserializer
        )
    }

    internal class CustomCookieJar : CookieJar {

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