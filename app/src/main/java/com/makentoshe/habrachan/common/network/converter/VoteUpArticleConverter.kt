package com.makentoshe.habrachan.common.network.converter

import com.google.gson.Gson
import com.makentoshe.habrachan.common.entity.article.VoteArticleResponse
import okhttp3.ResponseBody

class VoteUpArticleConverter {

    fun convertBody(body: ResponseBody): VoteArticleResponse {
        return Gson().fromJson(body.string(), VoteArticleResponse.Success::class.java)
    }

    fun convertError(body: ResponseBody): VoteArticleResponse {
        return Gson().fromJson(body.string(), VoteArticleResponse.Error::class.java)
    }
}