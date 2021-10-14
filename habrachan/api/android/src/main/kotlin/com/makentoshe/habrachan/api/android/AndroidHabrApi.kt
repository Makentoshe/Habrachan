package com.makentoshe.habrachan.api.android

import com.makentoshe.habrachan.api.HabrApiPath
import com.makentoshe.habrachan.api.articles.api.HabrArticlesApiBuilder
import com.makentoshe.habrachan.api.login.api.HabrLoginApiBuilder

object AndroidHabrApi : HabrApiPath {
    override val path: String = "https://habr.com"
}

fun AndroidHabrApi.articles(): HabrArticlesApiBuilder {
    return HabrArticlesApiBuilder(path.plus("/api/v1"))
}

fun AndroidHabrApi.login(): HabrLoginApiBuilder {
    return HabrLoginApiBuilder(path)
}


