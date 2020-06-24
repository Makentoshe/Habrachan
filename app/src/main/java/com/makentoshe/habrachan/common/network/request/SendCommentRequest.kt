package com.makentoshe.habrachan.common.network.request

import com.makentoshe.habrachan.common.entity.session.UserSession

data class SendCommentRequest(
    val client: String,
    val api: String,
    val token: String,
    val articleId: Int,
    val text: String,
    val parentId: Int = 0
) {
    constructor(userSession: UserSession, articleId: Int, text: String, parentId: Int) : this(
        userSession.clientKey, userSession.apiKey, userSession.tokenKey, articleId, text, parentId
    )
}