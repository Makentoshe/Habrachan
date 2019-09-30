package com.makentoshe.habrachan.common.model.network.users

import com.makentoshe.habrachan.common.model.network.HabrApi
import com.makentoshe.habrachan.common.model.network.Result

class GetUserByLogin(
    private val api: HabrApi, private val factory: GetUserByLoginConverterFactory
) {

    fun execute(request: GetUserByLoginRequest): Result.GetUserByLoginResponse {
        val response = api.getUserByLogin(
            clientKey = request.clientKey,
            accessToken = request.accessToken,
            apiKey = request.apiKey,
            login = request.login
        ).execute()

        return if (response.isSuccessful) {
            return response.body()!!
        } else {
            factory.converter.convert(response.errorBody()!!)
        }
    }
}