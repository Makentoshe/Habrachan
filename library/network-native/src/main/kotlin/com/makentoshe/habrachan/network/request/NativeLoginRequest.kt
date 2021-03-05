package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.network.UserSession

class NativeLoginRequest(
    override val userSession: UserSession, override val email: String, override val password: String
) : NativeRequest(), LoginRequest2