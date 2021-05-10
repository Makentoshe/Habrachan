package com.makentoshe.habrachan.entity

interface ArticleAuthor : UserId {
    val avatar: String
    val fullname: String?
    val login: String
}

fun articleAuthor(id: Int, avatar: String, login: String, fullname: String?) = object : ArticleAuthor {
    override val userId: Int = id
    override val avatar: String = avatar
    override val login: String = login
    override val fullname: String? = fullname
}