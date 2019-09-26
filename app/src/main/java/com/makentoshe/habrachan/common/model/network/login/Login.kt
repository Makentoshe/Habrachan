package com.makentoshe.habrachan.common.model.network.login

import com.makentoshe.habrachan.common.model.network.CookieStorage
import com.makentoshe.habrachan.common.model.network.HabrApi

class Login(
    private val api: HabrApi, private val cookieStorage: CookieStorage
) {

    fun execute(request: LoginRequest): LoginResult {
        return executeThroughApi(request) ?: errorResponse()
    }

    private fun executeThroughApi(request: LoginRequest): LoginResult? {
        val response = api.loginThroughApi(
            email = request.email,
            password = request.password,
            apiKey = request.apiKey ?: return null,
            clientKey = request.clientKey ?: return null,
            clientSecret = request.clientSecret ?: return null
        ).execute()

        return response.body()
    }

    private fun errorResponse(): LoginResult {
        return LoginResult(success = false)
    }

}