package com.makentoshe.habrachan.entity.android

import com.makentoshe.habrachan.delegate.requireStringReadonlyProperty
import com.makentoshe.habrachan.entity.article.tag.ArticleTag
import kotlinx.serialization.json.JsonPrimitive

val ArticleTag.title by requireStringReadonlyProperty("title")

internal fun articleTag(tag: String) = ArticleTag(mapOf("title" to JsonPrimitive(tag)))