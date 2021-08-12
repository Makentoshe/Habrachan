package com.makentoshe.habrachan.network

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.natives.Article
import com.makentoshe.habrachan.entity.natives.ArticleAuthor
import com.makentoshe.habrachan.entity.natives.NextPage
import com.makentoshe.habrachan.network.request.GetArticlesRequest2
import com.makentoshe.habrachan.network.response.GetArticlesResponse2
import com.makentoshe.habrachan.network.response.Pagination

data class NativeGetArticlesResponse(
    override val request: GetArticlesRequest2,
    override val articles: List<Article>,
    override val pagination: Pagination,
    val pages: Int,
    val serverTime: String,
    val sortedBy: String,
    val author: ArticleAuthor? = null
): GetArticlesResponse2 {

    /** Proxy class between raw json and [NativeGetArticlesResponse] */
    class Factory(
        @SerializedName("data")
        val articles: List<Article>,
        @SerializedName("next_page")
        val nextPage: NextPage,
        @SerializedName("pages")
        val pages: Int,
        @SerializedName("server_time")
        val serverTime: String,
        @SerializedName("sorted_by")
        val sortedBy: String,
        @SerializedName("author")
        val author: ArticleAuthor? = null
    ) {
        fun build(request: GetArticlesRequest2) : NativeGetArticlesResponse {
            val pagination = if (nextPage.url != null) {
                Pagination(Pagination.Next(nextPage.int, nextPage.url))
            } else {
                Pagination(null)
            }
            return NativeGetArticlesResponse(request, articles, pagination, pages, serverTime, sortedBy, author)
        }
    }
}