package com.makentoshe.habrachan.common.model.network.postsalt

data class GetTopRequest(
    val client: String,
    val token: String?,
    val api: String?,
    val page: Int,
    // Type can be daily, weekly, monthly, allTime
    val type: String
)