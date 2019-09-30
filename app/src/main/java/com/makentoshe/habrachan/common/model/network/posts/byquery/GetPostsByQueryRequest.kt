package com.makentoshe.habrachan.common.model.network.posts.byquery

data class GetPostsByQueryRequest(
    val query: String = "",
    val page: Int = 1,
    val hl: String? = null,
    val fl: String? = null
)