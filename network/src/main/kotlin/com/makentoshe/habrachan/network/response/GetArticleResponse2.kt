package com.makentoshe.habrachan.network.response

import com.makentoshe.habrachan.entity.Article
import com.makentoshe.habrachan.network.request.GetArticleRequest2

interface GetArticleResponse2 {
    val request: GetArticleRequest2
    val article: Article
}

fun getArticleResponse(request: GetArticleRequest2, article: Article) = object : GetArticleResponse2 {
    override val request = request
    override val article = article
}