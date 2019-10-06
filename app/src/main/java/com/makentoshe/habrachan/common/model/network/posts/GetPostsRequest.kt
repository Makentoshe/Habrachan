package com.makentoshe.habrachan.common.model.network.posts

data class GetPostsRequest(
    val page: Int = 1,
    val sort: String? = null,
    val hl: String? = null,
    val fl: String? = null,
    val date: String? = null,
    val custom: Boolean? = null
)

