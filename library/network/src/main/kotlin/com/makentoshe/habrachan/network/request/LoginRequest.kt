package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.network.UserSession

interface LoginRequest {
    val email: String
    val password: String
    val userSession: UserSession
}