package com.makentoshe.habrachan.common.model.network.users

data class GetUsersBySearchRequest(
    val page: Int = 1,
    val query: String = ""
)