package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.network.UserSession

// TODO replace user session to declared fields
data class MobileLoginRequest(
    override val userSession: UserSession, override val email: String, override val password: String
) : MobileRequest(), LoginRequest