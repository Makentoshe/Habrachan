package com.makentoshe.habrachan.entity

interface ArticleId {
    val articleId: Int
}

fun articleId(id: Int) = object: ArticleId {
    override val articleId: Int = id
}


