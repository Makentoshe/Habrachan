package com.makentoshe.habrachan.entity.article.author.component

interface AuthorId {
    val authorId: Int
}

fun authorId(authorId: Int) = object : AuthorId {
    override val authorId: Int = authorId
}