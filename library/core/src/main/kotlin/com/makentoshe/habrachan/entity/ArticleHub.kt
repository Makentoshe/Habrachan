package com.makentoshe.habrachan.entity

interface ArticleHub : HubId {
    val title: String
    val alias: String
}

fun articleHub(id: Int, title: String, alias: String) = object : ArticleHub {
    override val hubId: Int = id
    override val title: String = title
    override val alias: String = alias
}