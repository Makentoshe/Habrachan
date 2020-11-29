package com.makentoshe.habrachan.common.network.request

import com.makentoshe.habrachan.common.entity.session.UserSession

data class GetArticleRequest(
    val client: String,
    val api: String,
    val token: String,
    val id: Int
) {

    constructor(
        session: UserSession,
        id: Int
    ) : this(
        session.clientKey,
        session.apiKey,
        session.tokenKey,
        id
    )
}