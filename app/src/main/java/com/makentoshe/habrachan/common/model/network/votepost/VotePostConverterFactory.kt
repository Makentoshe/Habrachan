package com.makentoshe.habrachan.common.model.network.votepost

import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class VotePostConverterFactory : Converter.Factory() {

    private val converter by lazy {
        Converter<ResponseBody, VotePostResult> {
            Gson()
                .fromJson(it.string(), VotePostResult::class.java)
        }
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, VotePostResult>? {
        return if (type == VotePostResult::class.java) converter else null
    }
}