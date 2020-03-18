package com.makentoshe.habrachan.common.network.converter

import com.google.gson.Gson
import com.makentoshe.habrachan.common.entity.comment.GetCommentsResponse
import okhttp3.ResponseBody

class CommentsConverter {

    fun convertBody(body: ResponseBody): GetCommentsResponse {
        return Gson().fromJson(body.string(), GetCommentsResponse.Success::class.java)
    }

    fun convertError(body: ResponseBody): GetCommentsResponse {
        return Gson().fromJson(body.string(), GetCommentsResponse.Error::class.java)
    }
}
