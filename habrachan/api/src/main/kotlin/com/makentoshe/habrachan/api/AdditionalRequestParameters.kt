package com.makentoshe.habrachan.api

data class AdditionalRequestParameters(
    val headers: Map<String, String> = emptyMap(),
    val queries: Map<String, String> = emptyMap(),
) {

    constructor(
        headers: Map<String, String> = emptyMap(),
        queries: Map<String, String> = emptyMap(),
        cookies: Map<String, String> = emptyMap(),
    ) : this(headers = mergeCookiesIntoHeaders(cookies, headers), queries = queries)

    val cookies: Map<String, String>
        get() = headers["Cookie"]?.let { encodeCookiesString(it) } ?: emptyMap()

    private fun encodeCookiesString(value: String): Map<String, String> {
        val cookieEntries = value.split("; ")
        val cookies = cookieEntries.map { it.split("=") }.map { Pair(it[0], it[1]) }
        return cookies.toMap()
    }
}

private fun mergeCookiesIntoHeaders(
    cookies: Map<String, String>,
    headers: Map<String, String>,
) = HashMap(headers).apply {
    this["Cookie"] = "${headers["Cookie"]?.plus("; ") ?: ""}${cookies.cookies2string()}"
}

private fun Map<String, String>.cookies2string(): String {
    return entries.joinToString("; ") { "${it.key}=${it.value}" }
}
