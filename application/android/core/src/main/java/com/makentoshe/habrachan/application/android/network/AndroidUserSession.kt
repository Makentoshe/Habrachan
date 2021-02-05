package com.makentoshe.habrachan.application.android.network

import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetArticlesRequest

class AndroidUserSession(
    override val client: String,
    override val api: String,
    override val token: String?,
    val articlesRequestSpec: GetArticlesRequest.Spec
) : UserSession
