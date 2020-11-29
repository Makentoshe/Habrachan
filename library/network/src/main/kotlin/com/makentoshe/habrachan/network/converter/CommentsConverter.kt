package com.makentoshe.habrachan.network.converter

import com.google.gson.Gson
import com.makentoshe.habrachan.network.response.GetCommentsResponse
import okhttp3.ResponseBody

class CommentsConverter: Converter<GetCommentsResponse> {

    override fun convertBody(body: ResponseBody): Result<GetCommentsResponse> {
        val response = Gson().fromJson(body.string(), GetCommentsResponse::class.java)
        return Result.success(response)
    }

    override fun convertError(body: ResponseBody): Result<GetCommentsResponse> {
        return Result.failure(ConverterException(body.toString()))
    }
}
