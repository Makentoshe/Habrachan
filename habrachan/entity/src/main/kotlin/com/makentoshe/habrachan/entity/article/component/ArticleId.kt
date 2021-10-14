package com.makentoshe.habrachan.entity.article.component

interface ArticleId {
    val articleId: Int
}

fun articleId(id: Int) = object: ArticleId {
    override val articleId: Int = id
}
