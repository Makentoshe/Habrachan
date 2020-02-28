package com.makentoshe.habrachan.common.network.converter

import com.google.gson.Gson
import com.makentoshe.habrachan.common.entity.post.ArticlesResponse
import okhttp3.ResponseBody

class ArticlesConverter {

    fun convertBody(body: ResponseBody): ArticlesResponse.Success {
        return Gson().fromJson(body.string(), ArticlesResponse.Success::class.java)
    }

    fun convertError(body: ResponseBody): ArticlesResponse.Error {
        return ArticlesResponse.Error(body.string())
    }
}