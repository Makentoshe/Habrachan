package com.makentoshe.habrachan.common.model.network.posts

data class GetPostsBySearchRequest(
    val query: String = "",
    val page: Int = 1,
    val hl: String? = null,
    val fl: String? = null
)