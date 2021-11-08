package com.makentoshe.habrachan.application.android.screen.login.viewmodel

import com.makentoshe.habrachan.application.android.common.usersession.HabrSessionIdCookie
import com.makentoshe.habrachan.network.login.GetCookieResponse

sealed class GetCookieModel {

    data class Request(val response: GetCookieResponse) : GetCookieModel() {
        val referer: String get() = response.headers["Referer"]!!
        val state: String get() = response.queries["state"]!!
    }

    data class Login(val habrSessionIdCookie: HabrSessionIdCookie): GetCookieModel()
}