package com.makentoshe.habrachan.common.network.response

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.entity.article.NextPage

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
    ) : ArticlesResponse()

    data class Error(val json: String) : ArticlesResponse()

    companion object {
        /** Default count of an articles per page */
        const val DEFAULT_SIZE = 20
    }
}
