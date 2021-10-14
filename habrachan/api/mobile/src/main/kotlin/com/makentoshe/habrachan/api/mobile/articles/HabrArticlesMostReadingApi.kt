package com.makentoshe.habrachan.api.mobile.articles

import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.api.articles.api.HabrArticlesApiBuilder
import com.makentoshe.habrachan.api.articles.api.HabrArticlesArticleApi
import com.makentoshe.habrachan.api.articles.api.HabrArticlesMostReadingApiBuilder

fun HabrArticlesApiBuilder.mostReading(): HabrArticlesMostReadingApiBuilder {
    return HabrArticlesMostReadingApiBuilder(path.plus("/most-reading"))
}

fun HabrArticlesMostReadingApiBuilder.build(parameters: AdditionalRequestParameters): HabrArticlesArticleApi {
    return HabrArticlesArticleApi(path, parameters.queries, parameters.headers)
}
