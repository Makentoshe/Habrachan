package com.makentoshe.habrachan.network.login

import com.makentoshe.habrachan.entity.mobiles.login.LoginInitialState
import okhttp3.Cookie

/**
 * Mobile manager main response.
 * This login response will be useful only for mobile managers.
 */
data class WebMobileLoginResponse(
    val request: WebMobileLoginRequest,
    val cookies: List<Cookie>,
    val raw: String,
    val initialState: LoginInitialState
)
