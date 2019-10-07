package com.makentoshe.habrachan.common.model.network.postsalt

data class GetFeedRequest(
    val client: String,
    val token: String,
    val page: Int
)