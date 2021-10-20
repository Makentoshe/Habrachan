package com.makentoshe.habrachan.entity.article.tag

import com.makentoshe.habrachan.AnyWithVolumeParameters
import kotlinx.serialization.json.JsonElement

data class ArticleTag(
    override val parameters: Map<String, JsonElement>
) : AnyWithVolumeParameters<JsonElement>
