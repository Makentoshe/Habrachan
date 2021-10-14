package com.makentoshe.habrachan.api.articles.api

import com.makentoshe.habrachan.api.HabrApiGet
import com.makentoshe.habrachan.api.HabrApiPath

data class HabrArticlesArticleApiBuilder(override val path: String): HabrApiPath

data class HabrArticlesArticleApi(
    override val path: String,
    override val queries: Map<String, String>,
    override val headers: Map<String, String>
): HabrApiGet
