package com.makentoshe.habrachan.application.android.screen.login.viewmodel

sealed class GetCookieSpec {

    object Request: GetCookieSpec()

    data class Login(val cookies: List<String>): GetCookieSpec()
}
