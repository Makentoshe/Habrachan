package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.api.MobileLoginApi
import com.makentoshe.habrachan.network.request.MobileLoginRequest
import com.makentoshe.habrachan.network.response.LoginResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * Performs a login using mobile api.
 *
 * Should use with ui, that works like a web browser, e.g. WebView.
 */
class WebMobileLoginManager private constructor(
    private val loginApi: MobileLoginApi,
) {

    fun request(userSession: UserSession, email: String, password: String): MobileLoginRequest {
        return MobileLoginRequest(userSession, email, password)
    }

    // TODO retrieve and solve captcha
    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun login(request: MobileLoginRequest, htmlProcess: suspend (MobileLoginRequest, String, String) -> LoginResponse.MobileResponse): Result<LoginResponse> {
        try {
            // get cookies. consumer, state and referer
            val cookieResponse = loginApi.getLoginCookies().execute()
            val referer = cookieResponse.raw().request.url
            val state = referer.queryParameter("state") ?: ""
            val consumer = referer.queryParameter("consumer") ?: ""
            val url = "https://account.habr.com/login/?state=$state&consumer=$consumer&hl=${request.userSession.habrLanguage}"

            return Result.success(LoginResponse(request, mobileResponse = htmlProcess(request, state, consumer), user = null))
        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }

    /**
     * pass user manager for second request that allows to return user in login response
     * if userManager == null so user is also will be null
     */
    class Builder(client: OkHttpClient) {

        private val baseUrl = "https://account.habr.com"
        private val client = client.newBuilder().build()

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() =
            WebMobileLoginManager(getRetrofit().create(MobileLoginApi::class.java))
    }
}