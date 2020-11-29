package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.network.UserSession

class UserArticlesRequest(
    val session: UserSession,
    val user: String,
    val page: Int,
    val include: String? = null,
    val exclude: String? = null
)