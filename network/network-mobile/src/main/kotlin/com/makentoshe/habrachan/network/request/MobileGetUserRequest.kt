package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.network.UserSession

data class MobileGetUserRequest(
    override val userSession: UserSession, override val username: String
) : MobileRequest(), GetUserRequest