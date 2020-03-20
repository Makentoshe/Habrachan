package com.makentoshe.habrachan.common.network.converter

import com.google.gson.Gson
import com.makentoshe.habrachan.common.entity.comment.VoteCommentResponse
import com.makentoshe.habrachan.common.network.request.VoteCommentRequest
import okhttp3.ResponseBody

class VoteCommentConverter(private val request: VoteCommentRequest) {

    fun convertBody(body: ResponseBody): VoteCommentResponse {
        return Gson().fromJson(body.string(), VoteCommentResponse.Success::class.java).copy(request = request)
    }

    fun convertError(body: ResponseBody): VoteCommentResponse {
        return Gson().fromJson(body.string(), VoteCommentResponse.Error::class.java).copy(request = request)
    }
}