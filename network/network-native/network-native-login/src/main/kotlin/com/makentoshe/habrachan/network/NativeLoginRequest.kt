package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.network.request.LoginRequest

class NativeLoginRequest(
    override val userSession: UserSession, override val email: String, override val password: String
) : NativeRequest(), LoginRequest