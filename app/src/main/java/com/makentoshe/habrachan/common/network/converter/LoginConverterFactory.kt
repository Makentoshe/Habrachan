package com.makentoshe.habrachan.common.network.converter

import com.google.gson.Gson
import com.makentoshe.habrachan.common.entity.login.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class LoginConverterFactory : Converter.Factory() {

    private val converter = Converter<ResponseBody, LoginResponse> {
        Gson().fromJson(it.string(), LoginResponse::class.java)
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, LoginResponse>? {
        return if (type == LoginResponse::class.java) converter else null
    }
}
