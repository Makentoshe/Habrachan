package com.makentoshe.habrachan.entity

interface CommentAuthor {
    val login: String
}

fun commentAuthor(login: String) = object : CommentAuthor {
    override val login = login
}