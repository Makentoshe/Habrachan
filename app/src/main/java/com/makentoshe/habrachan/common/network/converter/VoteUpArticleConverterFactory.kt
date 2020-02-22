package com.makentoshe.habrachan.common.network.converter

import com.google.gson.Gson
import com.makentoshe.habrachan.common.entity.article.VoteArticleResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class VoteUpArticleConverterFactory: Converter.Factory() {

    private val converter = Converter<ResponseBody, VoteArticleResponse> {
        Gson().fromJson(it.string(), VoteArticleResponse::class.java)
    }

    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: Retrofit
    ): Converter<ResponseBody, VoteArticleResponse>? {
        return if (type == VoteArticleResponse::class.java) converter else null
    }
}
