package com.makentoshe.habrachan.application.common.arena.article.get

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.delegate.requireReadonlyProperty
import com.makentoshe.habrachan.delegate.requireStringReadonlyProperty
import com.makentoshe.habrachan.entity.article.hub.component.HubId
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

data class ArticleHubFromArena(
    override val parameters: Map<String, JsonElement>,
) : AnyWithVolumeParameters<JsonElement>

val ArticleHubFromArena.hubId by requireReadonlyProperty("id") { jsonElement ->
    HubId(jsonElement.jsonPrimitive.int)
}

val ArticleHubFromArena.title by requireStringReadonlyProperty("title")

val ArticleHubFromArena.alias by requireStringReadonlyProperty("alias")
