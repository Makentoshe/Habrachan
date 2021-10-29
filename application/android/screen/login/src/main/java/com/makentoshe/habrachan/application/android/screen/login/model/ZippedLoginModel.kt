package com.makentoshe.habrachan.application.android.screen.login.model

import com.makentoshe.habrachan.network.login.LoginSession

data class ZippedLoginModel(val cookies: List<String>, val loginSession: LoginSession)