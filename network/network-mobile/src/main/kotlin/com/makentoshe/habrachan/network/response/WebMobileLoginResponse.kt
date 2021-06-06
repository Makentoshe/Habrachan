package com.makentoshe.habrachan.network.response

import com.makentoshe.habrachan.entity.mobiles.login.LoginInitialState
import com.makentoshe.habrachan.network.request.WebMobileLoginRequest
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
