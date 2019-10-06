package com.makentoshe.habrachan.common.model.network.votepost

import com.makentoshe.habrachan.common.model.network.Result
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class VotePostConverterFactory : Converter.Factory() {

    val converter = VotePostConverter()

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, Result.VotePostResponse>? {
        return if (type == Result.VotePostResponse::class.java) converter else null
    }
}

