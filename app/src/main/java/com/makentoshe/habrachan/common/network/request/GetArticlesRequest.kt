package com.makentoshe.habrachan.common.network.request

data class GetArticlesRequest(
    val client: String,
    val api: String,
    val token: String,
    val page: Int,
    val spec: String
)