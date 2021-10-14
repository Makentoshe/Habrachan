package com.makentoshe.habrachan.entity.android

import com.makentoshe.habrachan.delegate.requireReadonlyProperty
import com.makentoshe.habrachan.delegate.requireStringReadonlyProperty
import com.makentoshe.habrachan.entity.article.hub.ArticleHub
import com.makentoshe.habrachan.entity.article.hub.component.HubId
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

val ArticleHub.hubId by requireReadonlyProperty("id") { jsonElement ->
    HubId(jsonElement.jsonPrimitive.int)
}

val ArticleHub.title by requireStringReadonlyProperty("title")
