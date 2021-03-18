package com.makentoshe.habrachan.network.response

import com.makentoshe.habrachan.entity.Article

// TODO(medium) add request
interface GetArticlesResponse2 {
    val articles: List<Article>
}

fun getArticlesResponse(articles: List<Article>) = object: GetArticlesResponse2 {
    override val articles: List<Article> = articles
}
