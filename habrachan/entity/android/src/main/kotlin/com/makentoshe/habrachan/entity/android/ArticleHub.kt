package com.makentoshe.habrachan.entity.android

import com.makentoshe.habrachan.delegate.requireReadonlyProperty
import com.makentoshe.habrachan.delegate.requireStringReadonlyProperty
import com.makentoshe.habrachan.entity.article.hub.ArticleHubPropertiesDelegate
import com.makentoshe.habrachan.entity.article.hub.component.HubId
import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.AnyWithVolumeParameters
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

data class ArticleHubPropertiesDelegateImpl(
    override val parameters: Map<String, JsonElement>
): ArticleHubPropertiesDelegate, AnyWithVolumeParameters<JsonElement> {

    /** Required to make requests and storing in database as a primary key */
    override val hubId by requireReadonlyProperty("id") { jsonElement ->
        HubId(jsonElement.jsonPrimitive.int)
    }

    override val title by requireStringReadonlyProperty("title")
}
