package com.makentoshe.habrachan.network.converter

import com.google.gson.Gson
import com.makentoshe.habrachan.network.response.VoteCommentResponse
import okhttp3.ResponseBody

class VoteCommentConverter : Converter<VoteCommentResponse> {

    override fun convertBody(body: ResponseBody): Result<VoteCommentResponse> {
        val response = Gson().fromJson(body.string(), VoteCommentResponse::class.java)
        return Result.success(response)
    }

    override fun convertError(body: ResponseBody): Result<VoteCommentResponse> {
        return Result.failure(ConverterException(body.toString()))
    }
}