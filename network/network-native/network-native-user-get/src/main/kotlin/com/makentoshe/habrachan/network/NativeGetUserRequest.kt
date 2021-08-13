package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.network.request.GetUserRequest
import com.makentoshe.habrachan.network.request.NativeRequest

class NativeGetUserRequest(
    override val userSession: UserSession, override val username: String
) : NativeRequest(), GetUserRequest