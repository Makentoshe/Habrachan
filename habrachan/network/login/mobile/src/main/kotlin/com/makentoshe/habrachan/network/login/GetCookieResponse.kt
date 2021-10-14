package com.makentoshe.habrachan.network.login

data class GetCookieResponse(
    val request: GetCookieRequest,
    val queries: Map<String, String>,
    val headers: Map<String, String>
)