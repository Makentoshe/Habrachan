package com.makentoshe.habrachan.common.model.network

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class StringConverterFactory : Converter.Factory() {

    private val converter by lazy {
        Converter<ResponseBody, String> { it.string() }
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, String>? {
        return if (type == String::class.java) converter else null
    }
}