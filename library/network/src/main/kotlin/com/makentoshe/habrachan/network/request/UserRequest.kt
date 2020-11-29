package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.network.UserSession

data class UserRequest(
    val userSession: UserSession,
    val name: String,
    val include: String? = null,
    val exclude: String? = null
)