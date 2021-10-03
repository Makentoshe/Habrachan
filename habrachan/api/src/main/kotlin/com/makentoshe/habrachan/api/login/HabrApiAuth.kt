package com.makentoshe.habrachan.api.login

import com.makentoshe.habrachan.api.HabrApiUrl

data class HabrApiAuth(override val url: String) : HabrApiUrl

fun HabrApiAuth.get(): HabrApiAuthGet {
    return HabrApiAuthGet(url, headers = emptyMap())
}

data class HabrApiAuthGet(override val url: String, val headers: Map<String, String>): HabrApiUrl

