package com.makentoshe.habrachan.common.network.request

import com.makentoshe.habrachan.common.entity.session.ArticlesRequestSpec
import com.makentoshe.habrachan.common.entity.session.UserSession

data class GetArticlesRequest(
    val client: String,
    val api: String,
    val token: String,
    val page: Int,
    val spec: String,
    val sort: String? = null,
    val include: String? = null,
    val exclude: String? = null
) {

    constructor(
        userSession: UserSession,
        page: Int,
        spec: ArticlesRequestSpec
    ) : this(
        userSession.clientKey,
        userSession.apiKey,
        userSession.tokenKey,
        page,
        spec.request,
        spec.sort,
        spec.include,
        spec.exclude
    )
}