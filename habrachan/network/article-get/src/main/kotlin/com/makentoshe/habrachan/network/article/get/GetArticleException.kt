package com.makentoshe.habrachan.network.article.get

import com.makentoshe.habrachan.AnyWithVolumeParameters
import kotlinx.serialization.json.JsonElement

data class GetArticleException(
    val request: GetArticleRequest,
    override val cause: Throwable?,
    override val parameters: Map<String, JsonElement>,
) : Exception(), AnyWithVolumeParameters<JsonElement>