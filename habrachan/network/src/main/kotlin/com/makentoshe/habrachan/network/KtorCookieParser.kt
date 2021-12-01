package com.makentoshe.habrachan.network

import io.ktor.http.*

class KtorCookieParser : CookieParser {

    override fun parseHeader(header: String): List<Cookie> {
        return parseClientCookiesHeader(header).map { (key, value) -> Cookie(key, value) }
    }

}
