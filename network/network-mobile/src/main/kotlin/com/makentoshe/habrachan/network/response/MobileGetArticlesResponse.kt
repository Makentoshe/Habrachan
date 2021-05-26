package com.makentoshe.habrachan.network.response

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.mobiles.Article
import com.makentoshe.habrachan.network.request.MobileGetArticlesRequest

data class MobileGetArticlesResponse(
    override val request: MobileGetArticlesRequest,
    override val pagination: Pagination,
    val articleIds: List<Int>,
    val articleRefs: Map<Int, Article>
): GetArticlesResponse2 {

    override val articles: List<Article>
        get() = articleRefs.values.toList()

    class Factory(
        @SerializedName("articleIds")
        val articleIds: List<Int>,
        @SerializedName("articleRefs")
        val articleRefs: Map<Int, Article>,
        @SerializedName("pagesCount")
        val pagesCount: Int // 50
    ) {
        fun build(request: MobileGetArticlesRequest): MobileGetArticlesResponse {
            val pagination = if (request.page < pagesCount) {
                Pagination(Pagination.Next(request.page + 1, null))
            } else {
                Pagination(null)
            }
            return MobileGetArticlesResponse(request, pagination, articleIds, articleRefs)
        }
    }
}
