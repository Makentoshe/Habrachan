package com.makentoshe.habrachan.common.network.converter

import com.google.gson.Gson
import com.makentoshe.habrachan.common.entity.comment.CommentsResponse
import com.makentoshe.habrachan.common.entity.post.PostResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class CommentsConverterFactory: Converter.Factory() {

    private val converter = Converter<ResponseBody, CommentsResponse> {
        Gson().fromJson(it.string(), CommentsResponse::class.java)
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, CommentsResponse>? {
        return if (type == CommentsResponse::class.java) converter else null
    }
}