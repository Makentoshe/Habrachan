package com.makentoshe.habrachan.entity.article.hub

import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.AnyWithVolumeParameters
import kotlinx.serialization.json.JsonElement

data class ArticleHub(
    override val parameters: Map<String, JsonElement>
) : AnyWithVolumeParameters<JsonElement>
