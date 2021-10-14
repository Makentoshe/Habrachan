package com.makentoshe.habrachan.entity.article.author.component

interface AuthorLogin {
    val authorLogin: String
}

fun authorLogin(authorLogin: String) = object : AuthorLogin {
    override val authorLogin: String = authorLogin
}