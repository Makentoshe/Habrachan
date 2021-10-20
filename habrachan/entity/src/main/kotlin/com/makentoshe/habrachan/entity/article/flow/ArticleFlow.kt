package com.makentoshe.habrachan.entity.article.flow

import com.makentoshe.habrachan.AnyWithVolumeParameters
import kotlinx.serialization.json.JsonElement

data class ArticleFlow(
    override val parameters: Map<String, JsonElement>
) : AnyWithVolumeParameters<JsonElement>
