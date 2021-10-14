package com.makentoshe.habrachan.entity.article.author

import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.AnyWithVolumeParameters
import kotlinx.serialization.json.JsonElement

data class ArticleAuthor(
    override val parameters: Map<String, JsonElement>
) : AnyWithVolumeParameters<JsonElement>
