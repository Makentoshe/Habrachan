package com.makentoshe.habrachan.network.exceptions

import com.makentoshe.habrachan.network.exception.LoginResponseException
import com.makentoshe.habrachan.network.request.LoginRequest

data class MobileLoginResponseException(
    override val request: LoginRequest,
    override val raw: String,
    override val email: String?,
    override val password: String?,
    override val other: String?
) : LoginResponseException()
