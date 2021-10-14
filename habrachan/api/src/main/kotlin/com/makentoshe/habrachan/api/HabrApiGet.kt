package com.makentoshe.habrachan.api

/** [HabrApiPath] that contains query params and headers for proper request building */
interface HabrApiGet : HabrApiPath {
    val headers: Map<String, String>
    val queries: Map<String, String>

    val queriesString: String
        get() = queries.toList().joinToString("&") { (key, value) -> "$key=$value" }
}


/** [HabrApiGet] that contains body for proper POST request building */
interface HabrApiPost : HabrApiGet {
    val body: String
}