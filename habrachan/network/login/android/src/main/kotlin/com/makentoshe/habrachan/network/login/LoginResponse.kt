package com.makentoshe.habrachan.network.login

import com.makentoshe.habrachan.network.login.entity.LoginSession

data class LoginResponse(val request: GetLoginRequest, val loginSession: LoginSession)