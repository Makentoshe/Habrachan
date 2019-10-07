package com.makentoshe.habrachan.common.model.network.postsalt

data class GetInterestingRequest(
    val client: String,
    val token: String?,
    val api: String?,
    val page: Int
)