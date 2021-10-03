package com.makentoshe.habrachan.api.article

import com.makentoshe.habrachan.api.HabrApiUrl

data class HabrApiArticleVote(override val url: String): HabrApiUrl

fun HabrApiUrl.post(): HabrApiArticleGet {
    return HabrApiArticleGet(url, headers = emptyMap())
}
