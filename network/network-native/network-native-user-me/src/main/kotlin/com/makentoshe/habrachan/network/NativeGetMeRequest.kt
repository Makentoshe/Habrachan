package com.makentoshe.habrachan.network

data class NativeGetMeRequest(
    val userSession: UserSession
) : NativeRequest()