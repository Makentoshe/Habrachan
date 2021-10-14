package com.makentoshe.habrachan.entity.mobile

import com.makentoshe.habrachan.delegate.requireReadonlyProperty
import com.makentoshe.habrachan.delegate.requireStringReadonlyProperty
import com.makentoshe.habrachan.entity.article.flow.ArticleFlow
import com.makentoshe.habrachan.entity.article.flow.component.FlowId
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

fun articleFlow(id: Int, title: String) = ArticleFlow(mapOf(
    "id" to JsonPrimitive(id),
    "title" to JsonPrimitive(title)
))

val ArticleFlow.flowId by requireReadonlyProperty("id") { jsonElement ->
    FlowId(jsonElement.jsonPrimitive.int)
}

val ArticleFlow.title by requireStringReadonlyProperty("title")
