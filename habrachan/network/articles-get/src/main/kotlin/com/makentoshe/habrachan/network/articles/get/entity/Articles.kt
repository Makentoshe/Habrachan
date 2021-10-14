package com.makentoshe.habrachan.network.articles.get.entity

import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.AnyWithVolumeParameters
import kotlinx.serialization.json.JsonElement

data class Articles(
    override val parameters: Map<String, JsonElement>
) : AnyWithVolumeParameters<JsonElement>
