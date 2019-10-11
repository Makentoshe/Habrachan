package com.makentoshe.habrachan.common.network.request

data class GetPostsRequest(
    val path1: String,
    val path2: String,
    val client: String,
    val token: String?,
    val api: String?,
    val page: Int,
    val query: String? = null,
    val sort: String? = null,
    val getArticle: Boolean? = null
)

