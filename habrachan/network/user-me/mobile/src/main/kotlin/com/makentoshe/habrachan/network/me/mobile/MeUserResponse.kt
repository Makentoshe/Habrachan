package com.makentoshe.habrachan.network.me.mobile

import com.makentoshe.habrachan.entity.me.mobile.NetworkMeUser

data class MeUserResponse(val request: MeUserRequest, val me: NetworkMeUser)
