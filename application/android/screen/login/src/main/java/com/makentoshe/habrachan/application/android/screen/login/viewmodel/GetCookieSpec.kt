package com.makentoshe.habrachan.application.android.screen.login.viewmodel

import java.net.HttpCookie

sealed class GetCookieSpec {

    object Request: GetCookieSpec()

    data class Login(val cookies: List<HttpCookie>): GetCookieSpec()
}
