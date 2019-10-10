package com.makentoshe.habrachan.common.network.request

data class GetPostRequest(
    val client: String,
    val token: String?,
    val api: String?,
    val id: Int
)