package com.makentoshe.habrachan.common.model.network.posts.bydate

import com.makentoshe.habrachan.common.model.network.Result
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class GetPostsByDateConverterFactory : Converter.Factory() {

    val converter = GetPostsByDateConverter()

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, Result.GetPostsByDateResponse>? {
        return if (type == Result.GetPostsByDateResponse::class.java) converter else null
    }

}
