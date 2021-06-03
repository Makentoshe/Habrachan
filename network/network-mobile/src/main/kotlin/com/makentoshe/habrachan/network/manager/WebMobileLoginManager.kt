package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.api.MobileLoginApi
import com.makentoshe.habrachan.network.fold
import com.makentoshe.habrachan.network.request.WebMobileLoginRequest
import com.makentoshe.habrachan.network.response.WebMobileLoginResponse
import okhttp3.*
import retrofit2.Retrofit

/**
 * This manager uses an external web tool for logging.
 *
 * Should use with ui, that works like a web browser, e.g. WebView.
 */
class WebMobileLoginManager private constructor(
    private val loginApi: MobileLoginApi, private val client: OkHttpClient, private val cookieJar: CustomCookieJar
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

            val loginScreenPassedUrl = request.external(loginScreenUrl)
            val loginScreenPassedRequest = Request.Builder().url(loginScreenPassedUrl).build()
            val loginScreenPassedResponse = client.newCall(loginScreenPassedRequest).execute()

            val string = loginScreenPassedResponse.body?.string().toString()
            return loginScreenPassedResponse.fold({
                Result.success(WebMobileLoginResponse(request, cookieJar.cookies.flatMap { it.value }, string))
            }, {
                Result.failure(Exception(it.string()))
            })
        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }

    class Builder(client: OkHttpClient) {

        private val cookieJar = CustomCookieJar()
        private val baseUrl = "https://account.habr.com"
        private val client = client.newBuilder().cookieJar(cookieJar).build()

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = WebMobileLoginManager(getRetrofit().create(MobileLoginApi::class.java), client, cookieJar)
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