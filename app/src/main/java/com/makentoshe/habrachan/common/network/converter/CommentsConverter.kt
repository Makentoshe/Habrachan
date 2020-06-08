package com.makentoshe.habrachan.common.network.converter

import com.google.gson.Gson
import com.makentoshe.habrachan.common.network.response.GetCommentsResponse
import okhttp3.ResponseBody

class CommentsConverter {

    fun convertBody(body: ResponseBody): GetCommentsResponse {
        val jsonString = body.string()
        return Gson().fromJson(jsonString, GetCommentsResponse.Success::class.java)
    }

    fun convertError(body: ResponseBody): GetCommentsResponse {
        val errorJsonString = body.string()
        return GetCommentsResponse.Error(errorJsonString)
    }
}
