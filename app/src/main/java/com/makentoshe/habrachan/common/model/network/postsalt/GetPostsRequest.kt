package com.makentoshe.habrachan.common.model.network.postsalt

data class GetPostsRequest(
    val path1: String,
    val path2: String,
    val client: String,
    val token: String?,
    val api: String?,
    val page: Int
)

