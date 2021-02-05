package com.makentoshe.habrachan.network.response

interface GetArticlesResponse2 {

    val articles: List<com.makentoshe.habrachan.entity.Article>

}

fun getArticlesResponse(articles: List<com.makentoshe.habrachan.entity.Article>) = object: GetArticlesResponse2 {
    override val articles: List<com.makentoshe.habrachan.entity.Article> = articles
}
