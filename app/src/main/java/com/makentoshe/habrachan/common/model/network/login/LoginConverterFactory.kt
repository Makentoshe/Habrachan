package com.makentoshe.habrachan.common.model.network.login

import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class LoginConverterFactory : Converter.Factory() {

    private val converter by lazy {
        Converter<ResponseBody, LoginResult> {
            Gson().fromJson(it.string(), LoginResult::class.java)
        }
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, LoginResult>? {
        return if (type == LoginResult::class.java) converter else null
    }
}