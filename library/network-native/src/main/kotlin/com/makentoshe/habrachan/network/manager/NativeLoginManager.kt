package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.api.NativeLoginApi
import com.makentoshe.habrachan.network.api.NativeUsersApi
import com.makentoshe.habrachan.network.deserializer.NativeGetUserDeserializer
import com.makentoshe.habrachan.network.deserializer.NativeLoginDeserializer
import com.makentoshe.habrachan.network.fold
import com.makentoshe.habrachan.network.request.NativeGetUserRequest
import com.makentoshe.habrachan.network.request.NativeLoginRequest
import com.makentoshe.habrachan.network.response.LoginResponse
import com.makentoshe.habrachan.network.response.NativeGetUserResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit

// TODO(high) refactor with NativeGetMeManager
class NativeLoginManager(
    private val loginApi: NativeLoginApi,
    private val loginDeserializer: NativeLoginDeserializer,
    private val usersApi: NativeUsersApi,
    private val userDeserializer: NativeGetUserDeserializer
) : LoginManager<NativeLoginRequest> {

    override fun request(userSession: UserSession, email: String, password: String): NativeLoginRequest {
        return NativeLoginRequest(userSession, email, password)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun login(request: NativeLoginRequest): Result<LoginResponse> = try {
        loginApi.login(
            request.userSession.client, request.userSession.api, request.email, request.password
        ).execute().fold({
            loginDeserializer.body(it.string())
        }, {
            loginDeserializer.error(it.string())
        }).fold({ nativeResponse ->
            // request self data using access token
            me(NativeGetUserRequest(request.userSession, ""), nativeResponse.accessToken).fold({
                Result.success(LoginResponse(request, nativeResponse = nativeResponse, user = it.user))
            }, {
                Result.failure(it)
            })
        }, {
            Result.failure(it)
        })
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private fun me(request: NativeGetUserRequest, token: String): Result<NativeGetUserResponse> {
        return usersApi.getMe(request.userSession.client, token).execute().fold({
            userDeserializer.body(request, it.string())
        }, {
            userDeserializer.error(request, it.string())
        })
    }

    class Builder(
        private val client: OkHttpClient,
        private val loginDeserializer: NativeLoginDeserializer,
        private val userDeserializer: NativeGetUserDeserializer
    ) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = NativeLoginManager(
            getRetrofit().create(NativeLoginApi::class.java),
            loginDeserializer,
            getRetrofit().create(NativeUsersApi::class.java),
            userDeserializer
        )
    }
}