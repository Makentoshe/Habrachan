package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.network.UserSession

interface LoginRequest2 {
    val email: String
    val password: String
    val userSession: UserSession
}