package com.makentoshe.habrachan.model.main.login

sealed class LoginData {
    /** Login and password */
    class Default(val email: String, val password: String) : LoginData()
    /** Token from OAuth2.0 */
    class Token(val token: String) : LoginData()
}