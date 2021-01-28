package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.network.UserSession

/** Request user self data  */
data class MeRequest(val userSession: UserSession, val include: String? = null, val exclude: String? = null)
