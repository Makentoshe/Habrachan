package com.makentoshe.habrachan.entity

interface ArticleTitle {
    val articleTitle: String
}

fun articleTitle(title: String) = object :  ArticleTitle {
    override val articleTitle: String = title
}
