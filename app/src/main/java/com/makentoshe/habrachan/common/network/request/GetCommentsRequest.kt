package com.makentoshe.habrachan.common.network.request

data class GetCommentsRequest(
    val client: String,
    val api: String,
    val token: String,
    val articleId: Int,
    val since: Int = -1
)