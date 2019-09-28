package com.makentoshe.habrachan.common.model.network.login

import com.makentoshe.habrachan.common.model.network.CookieStorage
import com.makentoshe.habrachan.common.model.network.HabrApi

class Login(
    private val api: HabrApi, private val cookieStorage: CookieStorage
) {

    fun execute(request: LoginRequest): LoginResult {
        val response = api.loginThroughApi(
            email = request.email,
            password = request.password,
            apiKey = request.apiKey,
            clientKey = request.clientKey,
            clientSecret = request.clientSecret
        ).execute()

        return response.body()?.copy(success = true) ?: createErrorResponse(
            code = response.code(), message = response.message() ?: response.errorBody()?.string()
        )
    }

    private fun createErrorResponse(code: Int, message: String?): LoginResult {
        return LoginResult(success = false, code = code, message = message)
    }

}