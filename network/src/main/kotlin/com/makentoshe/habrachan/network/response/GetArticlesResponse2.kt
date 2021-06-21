package com.makentoshe.habrachan.network.response

import com.makentoshe.habrachan.entity.Article
import com.makentoshe.habrachan.network.request.GetArticlesRequest2

interface GetArticlesResponse2 {
    val request: GetArticlesRequest2
    val articles: List<Article>

    /** Null if there is no next batch of data. */
    val pagination: Pagination
}

data class Pagination(val next: Next?) {
    data class Next(val number: Int, val url: String?)
}

fun getArticlesResponse(
    request: GetArticlesRequest2, articles: List<Article>, pagination: Pagination
) = object : GetArticlesResponse2 {
    override val request = request
    override val articles: List<Article> = articles
    override val pagination = pagination
}
