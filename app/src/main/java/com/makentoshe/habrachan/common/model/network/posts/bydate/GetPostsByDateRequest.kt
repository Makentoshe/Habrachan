package com.makentoshe.habrachan.common.model.network.posts.bydate

data class GetPostsByDateRequest(
    val date: String,
    val page: Int = 1,
    val hl: String? = null,
    val fl: String? = null
)
