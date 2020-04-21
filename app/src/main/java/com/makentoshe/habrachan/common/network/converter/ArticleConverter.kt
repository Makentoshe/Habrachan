package com.makentoshe.habrachan.common.network.converter

import com.google.gson.Gson
import com.makentoshe.habrachan.common.network.response.ArticleResponse
import okhttp3.ResponseBody

class ArticleConverter {

    fun convertBody(body: ResponseBody): ArticleResponse {
        return Gson().fromJson(body.string(), ArticleResponse.Success::class.java)
    }

    fun convertError(body: ResponseBody): ArticleResponse {
        return ArticleResponse.Error(body.string())
    }
}