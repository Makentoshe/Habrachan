package com.makentoshe.habrachan.api.article

import com.makentoshe.habrachan.api.HabrApiUrl

data class HabrApiArticleGet(override val url: String, val headers: Map<String, String>): HabrApiUrl