package com.makentoshe.habrachan.common.network.converter

import com.google.gson.Gson
import com.makentoshe.habrachan.common.entity.comment.VoteCommentResponse
import okhttp3.ResponseBody

class VoteCommentConverter {

    fun convertBody(body: ResponseBody): VoteCommentResponse {
        return Gson().fromJson(body.string(), VoteCommentResponse.Success::class.java)
    }

    fun convertError(body: ResponseBody): VoteCommentResponse {
        return Gson().fromJson(body.string(), VoteCommentResponse.Error::class.java)
    }
}