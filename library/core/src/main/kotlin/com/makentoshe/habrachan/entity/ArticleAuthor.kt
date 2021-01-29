package com.makentoshe.habrachan.entity

interface ArticleAuthor : UserId {
    val avatar: String
    val fullname: String?
    val login: String
}