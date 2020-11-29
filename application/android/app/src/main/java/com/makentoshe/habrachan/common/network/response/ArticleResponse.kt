package com.makentoshe.habrachan.common.network.response

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.entity.Article

sealed class ArticleResponse {
    data class Success(
        @SerializedName("data")
        val article: Article,
        @SerializedName("server_time")
        val serverTime: String
    ): ArticleResponse()

    data class Error(val json: String): ArticleResponse()
}