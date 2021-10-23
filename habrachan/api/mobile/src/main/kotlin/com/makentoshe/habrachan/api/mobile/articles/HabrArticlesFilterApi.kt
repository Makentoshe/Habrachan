package com.makentoshe.habrachan.api.mobile.articles

import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.api.articles.ArticlesFilter
import com.makentoshe.habrachan.api.articles.ArticlesPeriod
import com.makentoshe.habrachan.api.articles.ArticlesSort
import com.makentoshe.habrachan.api.articles.api.HabrArticlesApiBuilder
import com.makentoshe.habrachan.api.articles.api.HabrArticlesFilterApi
import com.makentoshe.habrachan.api.articles.api.HabrArticlesFilterApiBuilder

/** Works with */
object DateArticlesSort : ArticlesSort("date")

object RatingArticlesSort : ArticlesSort("rating")

fun companyArticlesFilter(value: String) = ArticlesFilter.QueryArticlesFilter("company", value)

fun hubArticlesFilter(value: String) = ArticlesFilter.QueryArticlesFilter("hub", value)

fun periodArticlesFilter(period: ArticlesPeriod) = ArticlesFilter.QueryArticlesFilter("period", period.value)

fun sortArticlesFilter(value: ArticlesSort) = ArticlesFilter.QueryArticlesFilter("sort", value.value)

fun pageArticlesFilter(value: Int) = ArticlesFilter.QueryArticlesFilter("page", value.toString())

fun mostReadingArticlesFilter() = ArticlesFilter.PathArticlesFilter("most-reading")

fun HabrArticlesApiBuilder.filter(vararg filters: ArticlesFilter): HabrArticlesFilterApiBuilder {
    return HabrArticlesFilterApiBuilder(path, filters.toList())
}

fun HabrArticlesFilterApiBuilder.build(parameters: AdditionalRequestParameters): HabrArticlesFilterApi {
    val filterQueries = filters.filterIsInstance<ArticlesFilter.QueryArticlesFilter>()
    val apiQueries = parameters.queries.plus(filterQueries.map { it.pair })

    val filterPaths = filters.filterIsInstance<ArticlesFilter.PathArticlesFilter>()
    val apiPath = this.path.plus("/${filterPaths.joinToString("/") { it.value }}")

    return HabrArticlesFilterApi(apiPath, apiQueries, parameters.headers)
}
