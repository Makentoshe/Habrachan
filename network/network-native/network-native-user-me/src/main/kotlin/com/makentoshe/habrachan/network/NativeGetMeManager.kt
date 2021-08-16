package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.natives.api.NativeUsersApi
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit

class NativeGetMeManager internal constructor(
    private val api: NativeUsersApi,
    private val deserializer: NativeGetMeDeserializer
) {

    fun request(userSession: UserSession): NativeGetMeRequest {
        return NativeGetMeRequest(userSession)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun me(request: NativeGetMeRequest): Result<NativeGetMeResponse> = try {
        getMeApi(request).deserialize(request)
    } catch (exception: Exception) {
        Result.failure(NativeGetMeException(request, message = exception.localizedMessage, cause = exception))
    }

    private fun getMeApi(request: NativeGetMeRequest): Response<ResponseBody> {
        return api.getMe(request.userSession.client, request.userSession.token).execute()
    }

    private fun Response<ResponseBody>.deserialize(request: NativeGetMeRequest): Result<NativeGetMeResponse> {
        return fold({ successBody ->
            deserializer.body(request, successBody.string())
        }, { failureBody ->
            deserializer.error(request, failureBody.string())
        })
    }

    class Builder(private val client: OkHttpClient) {

        private val baseUrl = "https://habr.com/"
        private val deserializer = NativeGetMeDeserializer()

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = NativeGetMeManager(getRetrofit().create(NativeUsersApi::class.java), deserializer)
    }
}