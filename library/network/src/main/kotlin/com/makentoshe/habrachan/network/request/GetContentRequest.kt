package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.network.UserSession

data class GetContentRequest(val userSession: UserSession, val url: String) : Request {

    // Should not being used for this request
    override val domain: String
        get() = "https://habr.com"
}