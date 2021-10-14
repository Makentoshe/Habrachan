package com.makentoshe.habrachan.entity.article.component

interface ArticleTitle {
    val articleTitle: String
}

fun articleTitle(title: String) = object :  ArticleTitle {
    override val articleTitle: String = title
}
