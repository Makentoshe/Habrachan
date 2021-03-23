package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.api.NativeLoginApi
import com.makentoshe.habrachan.network.deserializer.NativeLoginDeserializer
import com.makentoshe.habrachan.network.fold
import com.makentoshe.habrachan.network.request.NativeGetUserRequest
import com.makentoshe.habrachan.network.request.NativeLoginRequest
import com.makentoshe.habrachan.network.response.LoginResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit

// TODO(high) refactor with NativeGetMeManager
class NativeLoginManager(
    private val api: NativeLoginApi,
    private val deserializer: NativeLoginDeserializer,
    private val manager: NativeGetMeManager?
) : LoginManager<NativeLoginRequest> {

    override fun request(userSession: UserSession, email: String, password: String): NativeLoginRequest {
        return NativeLoginRequest(userSession, email, password)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun login(request: NativeLoginRequest): Result<LoginResponse> {
        val nativeResponse=  api.login(
            request.userSession.client, request.userSession.api, request.email, request.password
        ).execute().fold({
            deserializer.body(it.string())
        }, {
            deserializer.error(it.string())
        })

        return manager?.me(manager.request(request.userSession))?.fold({
            nativeResponse.map { nativeResponse -> LoginResponse(request, nativeResponse = nativeResponse, user = it.user) }
        }, {
            Result.failure(it)
        }) ?: nativeResponse.map { LoginResponse(request, nativeResponse = it, user = null) }
    }

    class Builder(private val client: OkHttpClient, private val deserializer: NativeLoginDeserializer, private val manager: NativeGetMeManager?) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = NativeLoginManager(getRetrofit().create(NativeLoginApi::class.java), deserializer, manager)
    }
}