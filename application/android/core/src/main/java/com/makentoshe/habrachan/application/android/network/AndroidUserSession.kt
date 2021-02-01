package com.makentoshe.habrachan.application.android.network

import com.makentoshe.habrachan.natives.network.request.GetArticlesRequest
import com.makentoshe.habrachan.network.UserSession

class AndroidUserSession(
    override val client: String,
    override val api: String,
    override val token: String?,
    override val articlesRequestSpec: GetArticlesRequest.Spec
) : UserSession
