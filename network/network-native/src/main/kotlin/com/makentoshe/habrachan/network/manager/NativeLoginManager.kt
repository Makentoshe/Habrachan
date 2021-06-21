package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.api.NativeLoginApi
import com.makentoshe.habrachan.network.deserializer.NativeLoginDeserializer
import com.makentoshe.habrachan.network.fold
import com.makentoshe.habrachan.network.request.NativeLoginRequest
import com.makentoshe.habrachan.network.response.LoginResponse
import com.makentoshe.habrachan.network.userSession
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class NativeLoginManager private constructor(
    private val api: NativeLoginApi,
    private val deserializer: NativeLoginDeserializer,
    private val manager: NativeGetMeManager?
) {

    fun request(userSession: UserSession, email: String, password: String): NativeLoginRequest {
        return NativeLoginRequest(userSession, email, password)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun login(request: NativeLoginRequest): Result<LoginResponse> = try {
        api.login(
            request.userSession.client, request.userSession.api, request.email, request.password
        ).execute().fold({ nativeResponseBody ->
            deserializer.body(nativeResponseBody.string()).fold({ nativeResponse ->
                val userSession = userSession(request.userSession, token = nativeResponse.accessToken)
                manager?.me(manager.request(userSession))?.fold({
                    Result.success(LoginResponse(request, nativeResponse = nativeResponse, user = it.user))
                }, {
                    Result.failure(it)
                }) ?: Result.success(LoginResponse(request, nativeResponse = nativeResponse, user = null))
            }, {
                // TODO(medium) rebox exception
                Result.failure(it)
            })

        }, {
            deserializer.error(request, it.string())
        })
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    class Builder(
        private val client: OkHttpClient,
        private val deserializer: NativeLoginDeserializer,
        private val manager: NativeGetMeManager?
    ) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = NativeLoginManager(getRetrofit().create(NativeLoginApi::class.java), deserializer, manager)
    }
}