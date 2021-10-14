package com.makentoshe.habrachan.network.articles.get

import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.AnyWithVolumeParameters
import kotlinx.serialization.json.JsonElement

data class GetArticlesException(
    val request: GetArticlesRequest,
    override val cause: Throwable?,
    override val parameters: Map<String, JsonElement>,
) : Exception(), AnyWithVolumeParameters<JsonElement>
