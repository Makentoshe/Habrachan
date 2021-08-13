package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetUserRequest
import com.makentoshe.habrachan.network.response.GetUserResponse

interface GetUserManager<Request: GetUserRequest> {

    fun request(userSession: UserSession, username: String): Request

    suspend fun user(request: Request): Result<GetUserResponse>

}