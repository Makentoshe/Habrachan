package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.api.NativeLoginApi
import com.makentoshe.habrachan.network.deserializer.NativeLoginDeserializer
import com.makentoshe.habrachan.network.fold
import com.makentoshe.habrachan.network.request.NativeLoginRequest
import com.makentoshe.habrachan.network.response.LoginResponse2
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class NativeLoginManager(
    private val api: NativeLoginApi, private val deserializer: NativeLoginDeserializer
) : LoginManager2<NativeLoginRequest> {

    override fun request(userSession: UserSession, email: String, password: String): NativeLoginRequest {
        return NativeLoginRequest(userSession, email, password)
    }

    override suspend fun login(request: NativeLoginRequest): Result<LoginResponse2> {
        val networkResponse =
            api.login(request.userSession.client, request.userSession.api, request.email, request.password).execute()
        val nativeResponse =
            networkResponse.fold({ deserializer.body(it.string()) }, { deserializer.error(it.string()) })
        return nativeResponse.fold({
            Result.success(LoginResponse2(request, nativeResponse = it))
        }, {
            Result.failure(it)
        })
    }

    class Builder(private val client: OkHttpClient, private val deserializer: NativeLoginDeserializer) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = NativeLoginManager(getRetrofit().create(NativeLoginApi::class.java), deserializer)
    }
}