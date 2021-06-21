package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.network.UserSession

// TODO replace user session by defined variables
interface GetUserRequest {
    val userSession: UserSession
    val username: String
}