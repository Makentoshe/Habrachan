package com.makentoshe.habrachan.common.network.request

import com.makentoshe.habrachan.common.entity.session.UserSession

data class UserRequest(
    val client: String,
    val token: String,
    val name: String,
    val include: String? = null,
    val exclude: String? = null
) {
    constructor(
        userSession: UserSession,
        name: String,
        include: String? = null,
        exclude: String? = null
    ) : this(
        userSession.clientKey,
        userSession.tokenKey,
        name,
        include,
        exclude
    )
}