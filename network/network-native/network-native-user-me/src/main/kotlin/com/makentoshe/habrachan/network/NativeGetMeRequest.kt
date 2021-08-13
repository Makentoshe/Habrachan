package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.network.request.NativeRequest

data class NativeGetMeRequest(
    val userSession: UserSession
) : NativeRequest()