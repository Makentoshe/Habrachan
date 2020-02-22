package com.makentoshe.habrachan.common.network.converter

import com.google.gson.Gson
import com.makentoshe.habrachan.common.entity.article.VoteUpArticleResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class VoteUpArticleConverterFactory: Converter.Factory() {

    private val converter = Converter<ResponseBody, VoteUpArticleResponse> {
        Gson().fromJson(it.string(), VoteUpArticleResponse::class.java)
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, VoteUpArticleResponse>? {
        return if (type == VoteUpArticleResponse::class.java) converter else null
    }
}
