package com.makentoshe.habrachan.common.model.network.posts

import com.makentoshe.habrachan.common.model.network.Result
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class GetPostsConverterFactory : Converter.Factory() {

    val converter = GetPostsConverter()

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, Result.GetPostsResponse>? {
        return if (type == Result.GetPostsResponse::class.java) converter else null
    }

}
