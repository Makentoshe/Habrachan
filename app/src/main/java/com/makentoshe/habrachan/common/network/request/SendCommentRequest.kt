package com.makentoshe.habrachan.common.network.request

data class SendCommentRequest(
    val client: String,
    val api: String,
    val token: String,
    val articleId: Int,
    val text: String,
    val parentId: Int = 0
)