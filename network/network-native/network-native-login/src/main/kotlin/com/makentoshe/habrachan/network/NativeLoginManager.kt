package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.functional.fold
import com.makentoshe.habrachan.functional.suspendFold
import com.makentoshe.habrachan.network.natives.api.NativeLoginApi
import com.makentoshe.habrachan.network.response.LoginResponse
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
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
    suspend fun login(request: NativeLoginRequest) : Result<LoginResponse> = try {
        val loginResponse = api.login(request.userSession.client, request.userSession.api, request.email, request.password).execute()
        loginResponse.fold({ successLoginResponseBody ->
            onLoginResponseSuccess(request, successLoginResponseBody)
        },{ failureLoginResponseBody ->
            onLoginResponseFailure(request, failureLoginResponseBody)
        })
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    private suspend fun onLoginResponseSuccess(request: NativeLoginRequest, response: ResponseBody): Result<LoginResponse> {
        return deserializer.body(response.string()).suspendFold({ nativeResponse ->
            onDeserializeSuccess(request, nativeResponse)
        },{ throwable ->
            onDeserializeFailure(request, throwable)
        })
    }

    private suspend fun onDeserializeSuccess(request: NativeLoginRequest, response: LoginResponse.NativeResponse): Result<LoginResponse> {
        val userSession = userSession(request.userSession, token = response.accessToken)
        return manager?.me(manager.request(userSession))?.fold({
            Result.success(LoginResponse(request, nativeResponse = response, user = it.user))
        }, {
            Result.failure(it)
        }) ?: Result.success(LoginResponse(request, nativeResponse = response, user = null))
    }

    // TODO(medium) rebox exception
    private fun onDeserializeFailure(request: NativeLoginRequest, throwable: Throwable) : Result<LoginResponse> {
        return Result.failure(throwable)
    }

    // TODO(medium) rebox exception
    private fun onLoginResponseFailure(request: NativeLoginRequest, response: ResponseBody) : Result<LoginResponse> {
        return deserializer.error(request, response.string())
    }

    class Builder(private val client: OkHttpClient, private val manager: com.makentoshe.habrachan.network.NativeGetMeManager?) {

        private val deserializer = NativeLoginDeserializer()
        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = NativeLoginManager(getRetrofit().create(NativeLoginApi::class.java), deserializer, manager)
    }
}