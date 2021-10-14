package com.makentoshe.habrachan.api.articles.api

import com.makentoshe.habrachan.api.HabrApiGet
import com.makentoshe.habrachan.api.HabrApiPath
import com.makentoshe.habrachan.api.articles.ArticlesFilter

data class HabrArticlesFilterApiBuilder(override val path: String, val filters: List<ArticlesFilter>): HabrApiPath

data class HabrArticlesFilterApi(
    override val path: String,
    override val queries: Map<String, String>,
    override val headers: Map<String, String>,
): HabrApiGet