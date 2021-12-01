package com.makentoshe.habrachan.network

interface CookieParser {

    fun parseHeader(header: String) : List<Cookie>

}
