package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.LoginRequest2
import com.makentoshe.habrachan.network.response.LoginResponse2

interface LoginManager2<Request: LoginRequest2> {

    fun request(userSession: UserSession, email: String, password: String): Request

    suspend fun login(request: Request): Unit//Result<LoginResponse2>
}