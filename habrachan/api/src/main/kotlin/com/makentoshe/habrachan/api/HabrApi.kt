package com.makentoshe.habrachan.api

import com.makentoshe.habrachan.api.article.HabrApiArticle
import com.makentoshe.habrachan.api.articles.HabrApiArticles
import com.makentoshe.habrachan.api.login.HabrApiAuth
import com.makentoshe.habrachan.api.login.HabrApiLogin

object HabrApi : HabrApiUrl {
    override val url: String = "https://habr.com"
}

fun HabrApi.article(articleId: Int): HabrApiArticle {
    return HabrApiArticle(url.plus("/kek/v2/articles/$articleId"))
}

fun HabrApi.articles(): HabrApiArticles {
    return HabrApiArticles(url.plus("/kek/v2/articles"))
}

fun HabrApi.auth(): HabrApiAuth {
    return HabrApiAuth(url.plus("/kek/v1/auth/habrahabr"))
}

fun HabrApi.login(state: String, consumer: String): HabrApiLogin {
    return HabrApiLogin(url.plus("?state=$state&consumer=$consumer"))
}
