package com.makentoshe.habrachan.common.model.network.posts.byquery

import com.makentoshe.habrachan.common.model.network.Result
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class GetPostsByQueryConverterFactory : Converter.Factory() {

    val converter = GetPostsByQueryConverter()

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, Result.GetPostsByQueryResponse>? {
        return if (type == Result.GetPostsByQueryResponse::class.java) converter else null
    }

}