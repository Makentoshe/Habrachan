package com.makentoshe.habrachan.api.articles.api

import com.makentoshe.habrachan.api.HabrApiGet
import com.makentoshe.habrachan.api.HabrApiPath

data class HabrArticlesMostReadingApiBuilder(override val path: String): HabrApiPath

data class HabrArticlesMostReadingApi(
    override val path: String,
    override val queries: Map<String, String>,
    override val headers: Map<String, String>
): HabrApiGet