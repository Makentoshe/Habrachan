package com.makentoshe.habrachan.common.model.network.posts

import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class PostsConverterFactory : Converter.Factory() {

    private val converter by lazy {
        Converter<ResponseBody, GetPostsResult> {
            Gson().fromJson(it.string(), GetPostsResult::class.java)
        }
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, GetPostsResult>? {
        return if (type == GetPostsResult::class.java) converter else null
    }

}
