package com.makentoshe.habrachan.common.entity.post

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.entity.posts.NextPage

sealed class ArticlesResponse {

    data class Success(
        @SerializedName("data")
        val data: List<Article>,
        @SerializedName("next_page")
        val nextPage: NextPage,
        @SerializedName("pages")
        val pages: Int,
        @SerializedName("server_time")
        val serverTime: String,
        @SerializedName("sorted_by")
        val sortedBy: String
    ): ArticlesResponse()

    data class Error(val json: String): ArticlesResponse()
}