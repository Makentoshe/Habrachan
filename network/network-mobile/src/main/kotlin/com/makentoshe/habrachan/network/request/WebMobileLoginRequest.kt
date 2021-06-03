package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.network.UserSession

/**
 * [userSession] is an object containing any user information for any api used in application.
 *
 * [external] is a lambda invokes for passing a web google recaptcha and input a user credentials.
 *
 * This function consumes an initial web login screen url https://account.habr.com/login/?state=...&consumer=...&hl=...
 * The captcha should be displayed and solved using any web view tool (Android's WebView, for example).
 *
 * This function should return a url https://habr.com/ac/entrance/?token=...&state=...&time=...&sign=...&utm_nooverride=...
 */
data class WebMobileLoginRequest(
    val userSession: UserSession,
    val external: suspend (String) -> String
): MobileRequest()