package com.makentoshe.habrachan.api.articles

import com.makentoshe.habrachan.api.HabrApiUrl
import com.makentoshe.habrachan.api.articles.filter.ArticlesFilter

data class HabrApiArticles(override val url: String): HabrApiUrl

fun HabrApiArticles.mostReading(): HabrApiArticlesMostReading {
    return HabrApiArticlesMostReading(url.plus("/most-reading"))
}

fun HabrApiArticles.filter(vararg filters: ArticlesFilter) : HabrApiArticlesFiltered {
    val query = filters.joinToString(separator = "&") { filter ->"${filter.key}=${filter.value}" }
    return HabrApiArticlesFiltered(url.plus("/?$query"))
}