package com.makentoshe.habrachan.api.android.articles

import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.api.articles.ArticlesFilter
import com.makentoshe.habrachan.api.articles.ArticlesPeriod
import com.makentoshe.habrachan.api.articles.api.HabrArticlesApiBuilder
import com.makentoshe.habrachan.api.articles.api.HabrArticlesFilterApi
import com.makentoshe.habrachan.api.articles.api.HabrArticlesFilterApiBuilder

/** Predefined filter for all possible articles */
fun allArticlesFilter() = ArticlesFilter.PathArticlesFilter("posts/all")

/** Predefined filter for interesting articles (in habr mind) */
fun interestingArticlesFilter() = ArticlesFilter.PathArticlesFilter("posts/interesting")

/** Predefined filter for top articles in the selected period */
fun topArticlesFilter(period: ArticlesPeriod) = ArticlesFilter.PathArticlesFilter("top/${period.value}")

fun pageArticlesFilter(value: Int) = ArticlesFilter.QueryArticlesFilter("page", value.toString())

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
