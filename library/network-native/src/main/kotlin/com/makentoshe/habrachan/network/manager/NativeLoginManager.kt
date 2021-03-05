package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.api.NativeLoginApi
import com.makentoshe.habrachan.network.request.NativeLoginRequest
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class NativeLoginManager(
    private val api: NativeLoginApi
) : LoginManager2<NativeLoginRequest> {

    override fun request(userSession: UserSession, email: String, password: String): NativeLoginRequest {
        return NativeLoginRequest(userSession, email, password)
    }

    // Todo should be completed lately
    override suspend fun login(request: NativeLoginRequest): Unit {
        val response = api.login(request.userSession.client, request.userSession.api, request.email, request.password).execute()
        println(response)
    }

    class Builder(private val client: OkHttpClient) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = NativeLoginManager(getRetrofit().create(NativeLoginApi::class.java))
    }
}