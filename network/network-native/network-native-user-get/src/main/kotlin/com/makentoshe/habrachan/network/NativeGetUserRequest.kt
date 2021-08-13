package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.network.request.GetUserRequest

class NativeGetUserRequest(
    override val userSession: UserSession, override val username: String
) : NativeRequest(), GetUserRequest