package com.makentoshe.habrachan.common.network.response

import com.makentoshe.habrachan.common.network.request.OAuthRequest
import okhttp3.Cookie

sealed class OAuthResponse {

    data class Interim(val request: OAuthRequest, val url: String, val cookies: Set<Cookie>): OAuthResponse()

    data class Error(val string: String): OAuthResponse()
}