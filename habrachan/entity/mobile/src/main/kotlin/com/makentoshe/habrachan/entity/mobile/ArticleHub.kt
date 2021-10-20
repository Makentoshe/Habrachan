package com.makentoshe.habrachan.entity.mobile

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.delegate.requireReadonlyProperty
import com.makentoshe.habrachan.delegate.requireStringReadonlyProperty
import com.makentoshe.habrachan.entity.article.hub.ArticleHubPropertiesDelegate
import com.makentoshe.habrachan.entity.article.hub.component.HubId
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

fun articleHubProperties(id: Int, title: String) = mapOf(
    "id" to JsonPrimitive(id),
    "title" to JsonPrimitive(title)
)

data class ArticleHubPropertiesDelegateImpl(
    override val parameters: Map<String, JsonElement>
) : ArticleHubPropertiesDelegate, AnyWithVolumeParameters<JsonElement> {

    /** Require2d to make requests and storing in database as a primary key */
    override val hubId by requireReadonlyProperty("id") { jsonElement ->
        HubId(jsonElement.jsonPrimitive.int)
    }

    override val title by requireStringReadonlyProperty("title")
}
