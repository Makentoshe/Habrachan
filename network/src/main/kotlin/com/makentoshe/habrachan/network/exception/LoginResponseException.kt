package com.makentoshe.habrachan.network.exception

import com.makentoshe.habrachan.network.request.LoginRequest

abstract class LoginResponseException: Throwable() {
    abstract val request: LoginRequest
    /** Full unparsed error */
    abstract val raw: String
    /** Error related to email field */
    abstract val email: String?
    /** Error related to password field */
    abstract val password: String?
    /** Error that not related with email or password */
    abstract val other: String?
}