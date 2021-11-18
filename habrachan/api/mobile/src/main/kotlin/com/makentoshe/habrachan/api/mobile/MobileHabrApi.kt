package com.makentoshe.habrachan.api.mobile

import com.makentoshe.habrachan.api.HabrApiPath
import com.makentoshe.habrachan.api.articles.api.HabrArticlesApiBuilder
import com.makentoshe.habrachan.api.login.api.HabrLoginApiBuilder
import com.makentoshe.habrachan.api.user.HabrUserApiBuilder

object MobileHabrApi : HabrApiPath {
    override val path: String = "https://habr.com"
}

fun MobileHabrApi.articles(): HabrArticlesApiBuilder {
    return HabrArticlesApiBuilder(path.plus("/kek/v2/articles"))
}

fun MobileHabrApi.login(): HabrLoginApiBuilder {
    return HabrLoginApiBuilder(path)
}

fun MobileHabrApi.user() : HabrUserApiBuilder {
    return HabrUserApiBuilder(path.plus("/kek/v2"))
}