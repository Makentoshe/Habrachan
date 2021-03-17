package com.makentoshe.habrachan.network.converter

import com.google.gson.Gson
import com.makentoshe.habrachan.network.response.GetCommentsResponse
import okhttp3.ResponseBody

fun convertCommentsSuccess(json: String): GetCommentsResponse.Factory {
    return Gson().fromJson<GetCommentsResponse.Factory>(json, GetCommentsResponse.Factory::class.java)
}
