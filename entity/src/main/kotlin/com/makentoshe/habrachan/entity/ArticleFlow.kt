package com.makentoshe.habrachan.entity

interface ArticleFlow : FlowId {
    val title: String
    val alias: String
}

fun articleFlow(id: Int, title: String, alias: String) = object : ArticleFlow {
    override val flowId: Int = id
    override val title: String = title
    override val alias: String = alias
}
