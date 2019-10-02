package com.makentoshe.habrachan.common.model.network.posts.bysort

data class GetPostsBySortRequest(
    val page: Int = 1,
    val sort: String,
    val hl: String? = null,
    val fl: String? = null
)
