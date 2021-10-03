package com.makentoshe.habrachan.api.article

import com.makentoshe.habrachan.api.HabrApiUrl

data class HabrApiArticle(override val url: String): HabrApiUrl

fun HabrApiArticle.get(): HabrApiArticleGet {
    return HabrApiArticleGet(url.plus("/?fl=ru%2Cen&hl=en"), headers = emptyMap())
}

fun HabrApiArticle.vote(vote: String): HabrApiArticleVote {
    return HabrApiArticleVote("/votes/$vote")
}

