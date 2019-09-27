package com.makentoshe.habrachan.common.model.network.posts

import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class PostsBySearchConverterFactory : Converter.Factory() {

    private val converter by lazy {
        Converter<ResponseBody, GetPostsBySearchResult> {
            Gson().fromJson(it.string(), GetPostsBySearchResult::class.java)
        }
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, GetPostsBySearchResult>? {
        return if (type == GetPostsBySearchResult::class.java) converter else null
    }

}