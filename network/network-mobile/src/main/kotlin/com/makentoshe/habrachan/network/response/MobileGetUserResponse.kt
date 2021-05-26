package com.makentoshe.habrachan.network.response

import com.makentoshe.habrachan.entity.User
import com.makentoshe.habrachan.network.request.GetUserRequest

data class MobileGetUserResponse(override val request: GetUserRequest, override val user: User) : GetUserResponse