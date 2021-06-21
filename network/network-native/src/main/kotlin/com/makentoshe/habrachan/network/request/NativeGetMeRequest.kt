package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.network.UserSession

data class NativeGetMeRequest(
    val userSession: UserSession
) : NativeRequest()