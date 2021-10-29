package com.makentoshe.habrachan.network.login

data class LoginResponse(val request: GetLoginRequest, val loginSession: LoginSession)