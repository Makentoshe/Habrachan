package com.makentoshe.habrachan.common.network.request

import com.makentoshe.habrachan.common.entity.session.UserSession

class UserArticlesRequest(
    val client: String,
    val token: String,
    val user: String,
    val include: String? = null,
    val exclude: String? = null
) {
    constructor(
        session: UserSession,
        user: String,
        include: String? = null,
        exclude: String? = null
    ) : this(
        session.clientKey,
        session.tokenKey,
        user,
        include,
        exclude
    )
}