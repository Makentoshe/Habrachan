package com.makentoshe.habrachan.network.converter

import com.google.gson.Gson
import com.makentoshe.habrachan.common.network.response.LoginResponse
import okhttp3.ResponseBody

class LoginConverter: Converter<LoginResponse> {

    override fun convertBody(body: ResponseBody): Result<LoginResponse> {
        val response = Gson().fromJson(body.string(), LoginResponse::class.java)
        return Result.success(response)
    }

    override fun convertError(body: ResponseBody): Result<LoginResponse> {
        return Result.failure(ConverterException(body.toString()))
    }
}
