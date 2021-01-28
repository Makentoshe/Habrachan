package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.network.UserSession

/** Request single article by [id] */
data class GetArticleRequest(val session: UserSession, val id: Int)