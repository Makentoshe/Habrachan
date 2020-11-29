package com.makentoshe.habrachan.network.response

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.Article

data class ArticleResponse(
    @SerializedName("data")
    val article: Article,
    @SerializedName("server_time")
    val serverTime: String
)