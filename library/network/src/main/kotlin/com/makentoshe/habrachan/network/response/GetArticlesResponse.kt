package com.makentoshe.habrachan.network.response

interface GetArticlesResponse {

    val articles: List<com.makentoshe.habrachan.entity.Article>

}

fun getArticlesResponse(articles: List<com.makentoshe.habrachan.entity.Article>) = object: GetArticlesResponse {
    override val articles: List<com.makentoshe.habrachan.entity.Article> = articles
}
