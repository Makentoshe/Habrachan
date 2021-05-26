package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.api.NativeUsersApi
import com.makentoshe.habrachan.network.deserializer.NativeGetMeDeserializer
import com.makentoshe.habrachan.network.fold
import com.makentoshe.habrachan.network.request.NativeGetMeRequest
import com.makentoshe.habrachan.network.response.NativeGetMeResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class NativeGetMeManager private constructor(
    private val api: NativeUsersApi,
    private val deserializer: NativeGetMeDeserializer
) {

    fun request(userSession: UserSession): NativeGetMeRequest {
        return NativeGetMeRequest(userSession)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun me(request: NativeGetMeRequest): Result<NativeGetMeResponse> {
        val token = request.userSession.token
        return api.getMe(request.userSession.client, token).execute().fold({
            deserializer.body(request, it.string())
        }, {
            deserializer.error(request, it.string())
        })
    }

    class Builder(
        private val client: OkHttpClient, private val deserializer: NativeGetMeDeserializer
    ) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = NativeGetMeManager(getRetrofit().create(NativeUsersApi::class.java), deserializer)
    }
}