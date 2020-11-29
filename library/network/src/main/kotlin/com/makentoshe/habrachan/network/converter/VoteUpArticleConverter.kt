package com.makentoshe.habrachan.network.converter

import com.google.gson.Gson
import com.makentoshe.habrachan.network.response.VoteArticleResponse
import okhttp3.ResponseBody

class VoteUpArticleConverter : Converter<VoteArticleResponse> {

    override fun convertBody(body: ResponseBody): Result<VoteArticleResponse> {
        val response = Gson().fromJson(body.string(), VoteArticleResponse::class.java)
        return Result.success(response)
    }

    override fun convertError(body: ResponseBody): Result<VoteArticleResponse> {
        return Result.failure(ConverterException(body.toString()))
    }
}