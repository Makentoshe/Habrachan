package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.network.UserSession

data class GetContentRequest(val userSession: UserSession, val url: String) : Request {

    // Should not being used for this request
    override val domain: String
        get() = "https://habr.com"

    object Avatar {
        const val stubMiddle = "https://habr.com/images/avatars/stub-user-middle.gif"
        const val stubSmall = "https://habr.com/images/avatars/stub-user-small.gif"
        val stubs = arrayOf(stubMiddle, stubSmall)
    }
}
