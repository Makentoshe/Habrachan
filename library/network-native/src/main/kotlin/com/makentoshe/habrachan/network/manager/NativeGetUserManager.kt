package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.api.NativeUsersApi
import com.makentoshe.habrachan.network.deserializer.NativeGetUserDeserializer
import com.makentoshe.habrachan.network.fold
import com.makentoshe.habrachan.network.request.NativeGetMeRequest
import com.makentoshe.habrachan.network.request.NativeGetUserRequest
import com.makentoshe.habrachan.network.response.NativeGetUserResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class NativeGetUserManager(
    private val api: NativeUsersApi,
    private val deserializer: NativeGetUserDeserializer
) : GetUserManager<NativeGetUserRequest> {

    override fun request(userSession: UserSession, username: String): NativeGetUserRequest {
        return NativeGetUserRequest(userSession, username)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun user(request: NativeGetUserRequest): Result<NativeGetUserResponse> = try {
        api.getUser(request.userSession.client, request.userSession.token, request.username).execute().fold({
            deserializer.body(request, it.string())
        }, {
            deserializer.error(request, it.string())
        })
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    class Builder(private val client: OkHttpClient, private val deserializer: NativeGetUserDeserializer) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = NativeGetUserManager(getRetrofit().create(NativeUsersApi::class.java), deserializer)
    }
}