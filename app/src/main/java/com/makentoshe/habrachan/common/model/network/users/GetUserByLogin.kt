package com.makentoshe.habrachan.common.model.network.users

import com.makentoshe.habrachan.common.model.network.HabrApi

class GetUserByLogin(private val api: HabrApi) {

    fun execute(request: GetUserByLoginRequest): GetUserByLoginResult {
        if (request.accessToken != null && request.apiKey != null) {

        }

        if (request.accessToken == null && request.apiKey == null) {
            return createErrorResult(400, "Token and api not specified. Authorisation required.")
        }

        val response = api.getUserByLogin(
            clientKey = request.clientKey,
            accessToken = request.accessToken,
            apiKey = request.apiKey,
            login = request.login
        ).execute()

        return response.body()?: createErrorResult(
            code = response.code(),
            message = response?.errorBody()?.string() ?: response.message()
        )
    }

    private fun createErrorResult(code: Int, message: String): GetUserByLoginResult {
        return GetUserByLoginResult(
            data = null,
            serverTime = null,
            code = code,
            message = message
        )
    }
}