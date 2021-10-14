package com.makentoshe.habrachan.entity.mobile

import com.makentoshe.habrachan.delegate.requireReadonlyProperty
import com.makentoshe.habrachan.delegate.requireStringReadonlyProperty
import com.makentoshe.habrachan.entity.article.hub.ArticleHub
import com.makentoshe.habrachan.entity.article.hub.component.HubId
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

fun articleHub(id: Int, title: String) = ArticleHub(mapOf(
    "id" to JsonPrimitive(id),
    "title" to JsonPrimitive("title")
))

val ArticleHub.hubId by requireReadonlyProperty("id") { jsonElement ->
    HubId(jsonElement.jsonPrimitive.int)
}

val ArticleHub.title by requireStringReadonlyProperty("title")
