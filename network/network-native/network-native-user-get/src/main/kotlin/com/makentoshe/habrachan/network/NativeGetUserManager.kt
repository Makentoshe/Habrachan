package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.manager.GetUserManager
import com.makentoshe.habrachan.network.natives.api.NativeUsersApi
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit

class NativeGetUserManager internal constructor(
    private val api: NativeUsersApi,
    private val deserializer: NativeGetUserDeserializer,
) : GetUserManager<NativeGetUserRequest> {

    override fun request(userSession: UserSession, username: String): NativeGetUserRequest {
        return NativeGetUserRequest(userSession, username)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun user(request: NativeGetUserRequest): Result<NativeGetUserResponse> = try {
        getUserApi(request).deserialize(request)
    } catch (exception: Exception) {
        Result.failure(NativeGetUserException(request, message = exception.localizedMessage, cause = exception))
    }

    private fun getUserApi(request: NativeGetUserRequest): Response<ResponseBody> {
        return api.getUser(request.userSession.client, request.userSession.token, request.username).execute()
    }

    private fun Response<ResponseBody>.deserialize(request: NativeGetUserRequest): Result<NativeGetUserResponse> {
        return fold({ successBody ->
            deserializer.success(request, successBody.string())
        }, { failureBody ->
            deserializer.failure(request, failureBody.string())
        })
    }

    class Builder(private val client: OkHttpClient) {

        private val baseUrl = "https://habr.com/"
        private val deserializer = NativeGetUserDeserializer()

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = NativeGetUserManager(getRetrofit().create(NativeUsersApi::class.java), deserializer)
    }
}