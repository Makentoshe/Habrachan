package com.makentoshe.habrachan.entity.article.hub

import com.makentoshe.habrachan.Require
import com.makentoshe.habrachan.entity.article.hub.component.HubId
import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.AnyWithVolumeParameters
import kotlinx.serialization.json.JsonElement

data class ArticleHub(
    override val parameters: Map<String, JsonElement>,
    val delegate: ArticleHubPropertiesDelegate,
) : AnyWithVolumeParameters<JsonElement>

interface ArticleHubPropertiesDelegate {

    /** Required to make requests and storing in database as a primary key */
    val hubId: Require<HubId>

    val title: Require<String>
}

val ArticleHub.title get() = delegate.title

val ArticleHub.hubId get() = delegate.hubId

/**
 * This factory method allows to declare a custom [ArticleHub] instance only with delegate.
 *
 * In this case any parameters delegates will not work correctly.
 * */
fun articleHub(
    hubId: Int,
    title: String,
    parameters: Map<String, JsonElement> = emptyMap(),
) = ArticleHub(parameters, object: ArticleHubPropertiesDelegate {
    override val hubId = Require(HubId(hubId))
    override val title = Require(title)
})