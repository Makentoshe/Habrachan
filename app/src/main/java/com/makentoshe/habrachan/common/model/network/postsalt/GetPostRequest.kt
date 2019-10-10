package com.makentoshe.habrachan.common.model.network.postsalt

data class GetPostRequest(
    val client: String,
    val token: String?,
    val api: String?,
    val id: Int
)