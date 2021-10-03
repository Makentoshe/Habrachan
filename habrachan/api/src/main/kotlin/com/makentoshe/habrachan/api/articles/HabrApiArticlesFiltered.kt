package com.makentoshe.habrachan.api.articles

import com.makentoshe.habrachan.api.HabrApiUrl

data class HabrApiArticlesFiltered(override val url: String): HabrApiUrl

fun HabrApiArticlesFiltered.get(): HabrApiArticlesFilteredGet {
    return HabrApiArticlesFilteredGet(url, headers = emptyMap())
}
