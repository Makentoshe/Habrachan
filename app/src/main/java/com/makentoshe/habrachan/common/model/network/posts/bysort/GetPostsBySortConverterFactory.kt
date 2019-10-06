package com.makentoshe.habrachan.common.model.network.posts.bysort

import com.makentoshe.habrachan.common.model.network.Result
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class GetPostsBySortConverterFactory : Converter.Factory() {

    val converter = GetPostsBySortConverter()

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, Result.GetPostsBySortResponse>? {
        return if (type == Result.GetPostsBySortResponse::class.java) converter else null
    }

}
