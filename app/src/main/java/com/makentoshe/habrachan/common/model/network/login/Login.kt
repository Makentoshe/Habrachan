package com.makentoshe.habrachan.common.model.network.login

import com.makentoshe.habrachan.common.model.network.HabrApi
import com.makentoshe.habrachan.common.model.network.Result

class Login(
    private val api: HabrApi,
    private val factory: LoginConverterFactory
) {

    fun execute(request: LoginRequest): Result.LoginResponse {
        val response = api.loginThroughApi(
            email = request.email,
            password = request.password,
            apiKey = request.apiKey,
            clientKey = request.clientKey,
            clientSecret = request.clientSecret
        ).execute()

        return if (response.isSuccessful) {
            response.body()!!
        } else {
            factory.converter.convert(response.errorBody()!!)
        }
    }
}