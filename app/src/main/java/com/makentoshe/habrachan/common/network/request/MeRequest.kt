package com.makentoshe.habrachan.common.network.request

import com.makentoshe.habrachan.common.entity.session.UserSession

data class MeRequest(
    val client: String,
    val token: String,
    val include: String? = null,
    val exclude: String? = null
) {
    constructor(
        userSession: UserSession,
        include: String? = null,
        exclude: String? = null
    ): this(
        userSession.clientKey,
        userSession.tokenKey,
        include,
        exclude
    )
}

