package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.network.UserSession

class NativeGetUserRequest(
    override val userSession: UserSession, override val username: String
) : NativeRequest(), GetUserRequest