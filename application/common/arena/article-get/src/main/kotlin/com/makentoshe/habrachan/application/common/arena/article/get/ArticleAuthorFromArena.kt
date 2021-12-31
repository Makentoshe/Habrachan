package com.makentoshe.habrachan.application.common.arena.article.get

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.delegate.optionReadonlyProperty
import com.makentoshe.habrachan.delegate.requireFloatReadonlyProperty
import com.makentoshe.habrachan.delegate.requireReadonlyProperty
import com.makentoshe.habrachan.delegate.requireStringReadonlyProperty
import com.makentoshe.habrachan.entity.article.author.component.AuthorAvatar
import com.makentoshe.habrachan.entity.article.author.component.authorId
import com.makentoshe.habrachan.entity.article.author.component.authorLogin
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

data class ArticleAuthorFromArena(
    override val parameters: Map<String, JsonElement>,
) : AnyWithVolumeParameters<JsonElement>

val ArticleAuthorFromArena.fullname by requireStringReadonlyProperty("fullname")

val ArticleAuthorFromArena.speciality by requireStringReadonlyProperty("speciality")

val ArticleAuthorFromArena.rating by requireFloatReadonlyProperty("rating")

val ArticleAuthorFromArena.authorId by requireReadonlyProperty("id") { jsonElement ->
    authorId(jsonElement.jsonPrimitive.int)
}

val ArticleAuthorFromArena.authorLogin by requireReadonlyProperty("alias") { jsonElement ->
    authorLogin(jsonElement.jsonPrimitive.content)
}

val ArticleAuthorFromArena.authorAvatar by optionReadonlyProperty("avatar") { jsonElement ->
    AuthorAvatar(jsonElement.jsonPrimitive.content)
}
