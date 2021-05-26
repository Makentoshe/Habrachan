package com.makentoshe.habrachan.network.response

import com.makentoshe.habrachan.entity.User
import com.makentoshe.habrachan.network.request.GetUserRequest

interface GetUserResponse {
    val request: GetUserRequest
    val user: User
}