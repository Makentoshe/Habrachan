package com.makentoshe.habrachan.common.network.converter

import com.google.gson.Gson
import com.makentoshe.habrachan.common.entity.post.PostResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class PostConverterFactory: Converter.Factory() {

    private val converter = Converter<ResponseBody, PostResponse> {
        Gson().fromJson(it.string(), PostResponse::class.java)
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, PostResponse>? {
        return if (type == PostResponse::class.java) converter else null
    }
}