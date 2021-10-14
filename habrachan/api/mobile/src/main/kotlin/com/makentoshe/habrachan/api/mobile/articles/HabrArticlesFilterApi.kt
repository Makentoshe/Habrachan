package com.makentoshe.habrachan.api.mobile.articles

import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.api.articles.ArticlesFilter
import com.makentoshe.habrachan.api.articles.ArticlesSort
import com.makentoshe.habrachan.api.articles.api.HabrArticlesApiBuilder
import com.makentoshe.habrachan.api.articles.api.HabrArticlesFilterApi
import com.makentoshe.habrachan.api.articles.api.HabrArticlesFilterApiBuilder
import com.makentoshe.habrachan.api.articles.filter.ArticlesPeriod

/** Works with */
object DateArticlesSort : ArticlesSort("date")

object RatingArticlesSort : ArticlesSort("rating")

fun companyArticlesFilter(value: String) = ArticlesFilter.QueryArticlesFilter("company", value)

fun hubArticlesFilter(value: String) = ArticlesFilter.QueryArticlesFilter("hub", value)

fun periodArticlesFilter(period: ArticlesPeriod) = ArticlesFilter.QueryArticlesFilter("period", period.value)

fun sortArticlesFilter(value: ArticlesSort) = ArticlesFilter.QueryArticlesFilter("sort", value.value)

fun pageArticlesFilter(value: Int) = ArticlesFilter.QueryArticlesFilter("page", value.toString())

fun HabrArticlesApiBuilder.filter(vararg filters: ArticlesFilter): HabrArticlesFilterApiBuilder {
    return HabrArticlesFilterApiBuilder(path, filters.toList())
}

fun HabrArticlesFilterApiBuilder.build(parameters: AdditionalRequestParameters): HabrArticlesFilterApi {
    return HabrArticlesFilterApi(path, parameters.queries.plus(filters.map { it.key to it.value }), parameters.headers)
}
