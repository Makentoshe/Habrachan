package com.makentoshe.habrachan.api

data class AdditionalRequestParameters(
    val headers: Map<String, String> = emptyMap(),
    val queries: Map<String, String> = emptyMap(),
    val cookies: Map<String, String> = emptyMap(),
)

