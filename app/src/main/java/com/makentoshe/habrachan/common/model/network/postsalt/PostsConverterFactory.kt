package com.makentoshe.habrachan.common.model.network.postsalt

import com.google.gson.Gson
import com.makentoshe.habrachan.common.model.network.postsalt.entity.PostsResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class PostsConverterFactory: Converter.Factory(){

    private val converter = Converter<ResponseBody, PostsResponse> {
        val json = it.string()
        return@Converter Gson().fromJson(json, PostsResponse::class.java)
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, PostsResponse>? {
        return if (type == PostsResponse::class.java) converter else null
    }
}