package com.makentoshe.habrachan.api.articles

import com.makentoshe.habrachan.api.HabrApiUrl

data class HabrApiArticlesMostReading(override val url: String): HabrApiUrl

fun HabrApiArticlesMostReading.get(): HabrApiArticlesMostReadingGet {
    return HabrApiArticlesMostReadingGet(url, headers = emptyMap())
}
