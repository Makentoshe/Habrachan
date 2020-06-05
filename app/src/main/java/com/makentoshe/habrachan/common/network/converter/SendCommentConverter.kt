package com.makentoshe.habrachan.common.network.converter

import com.google.gson.Gson
import com.makentoshe.habrachan.common.network.response.SendCommentResponse
import okhttp3.ResponseBody

class SendCommentConverter {

    fun convertBody(body: ResponseBody): SendCommentResponse.Success {
        return Gson().fromJson(body.string(), SendCommentResponse.Success::class.java)
    }

    fun convertError(body: ResponseBody): SendCommentResponse.Error {
        return SendCommentResponse.Error(body.string())
    }
}