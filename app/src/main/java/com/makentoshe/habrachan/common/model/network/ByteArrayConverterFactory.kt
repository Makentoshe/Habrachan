package com.makentoshe.habrachan.common.model.network

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class ByteArrayConverterFactory : Converter.Factory() {

    private val converter by lazy {
        Converter<ResponseBody, ByteArray> { it.bytes() }
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, ByteArray>? {
        return if (type.toString() == "byte[]" || type == ByteArray::class.java) converter else null
    }
}