package com.makentoshe.habrachan.application.android.screen.login.model

import com.makentoshe.habrachan.application.android.common.usersession.HabrSessionIdCookie
import com.makentoshe.habrachan.network.login.entity.LoginSession

data class ZippedLoginModel(val habrSessionIdCookie: HabrSessionIdCookie, val loginSession: LoginSession)