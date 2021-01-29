package com.makentoshe.habrachan.network.response

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.natives.Article
import com.makentoshe.habrachan.entity.natives.NextPage
import com.makentoshe.habrachan.entity.natives.User

interface GetArticlesResponse {

    val articles: List<com.makentoshe.habrachan.entity.Article>

    val pages: Int
}

data class ArticlesResponse(
    @SerializedName("data")
    val data: List<Article>,
    @SerializedName("next_page")
    val nextPage: NextPage,
    @SerializedName("pages")
    override val pages: Int,
    @SerializedName("server_time")
    val serverTime: String,
    @SerializedName("sorted_by")
    val sortedBy: String,
    @SerializedName("author")
    val author: User? = null
): GetArticlesResponse {
    override val articles: List<com.makentoshe.habrachan.entity.Article>
        get() = data
}

