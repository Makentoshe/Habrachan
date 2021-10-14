package com.makentoshe.habrachan.entity.mobile

import com.makentoshe.habrachan.delegate.requireFloatReadonlyProperty
import com.makentoshe.habrachan.delegate.requireReadonlyProperty
import com.makentoshe.habrachan.delegate.requireStringReadonlyProperty
import com.makentoshe.habrachan.entity.article.author.ArticleAuthor
import com.makentoshe.habrachan.entity.article.author.component.AuthorAvatar
import com.makentoshe.habrachan.entity.article.author.component.authorId
import com.makentoshe.habrachan.entity.article.author.component.authorLogin
import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.delegate.optionReadonlyProperty
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

fun articleAuthor(
    id: Int,
    login: String,
    avatar: String?,
    fullname: String?,
    speciality: String?,
    rating: Float,
) = ArticleAuthor(mapOf(
    "id" to JsonPrimitive(id),
    "alias" to JsonPrimitive(login),
    "avatar" to JsonPrimitive(avatar),
    "fullname" to JsonPrimitive(fullname),
    "speciality" to JsonPrimitive(speciality),
    "rating" to JsonPrimitive(rating)
))

val ArticleAuthor.authorId by requireReadonlyProperty(
    "id", map = { jsonElement -> authorId(jsonElement.jsonPrimitive.int) }
)

val ArticleAuthor.authorLogin by requireReadonlyProperty(
    "alias", map = { jsonElement -> authorLogin(jsonElement.jsonPrimitive.content) }
)

val ArticleAuthor.authorAvatar by optionReadonlyProperty(
    "avatar", map = { jsonElement -> AuthorAvatar(jsonElement.jsonPrimitive.content) }
)

val ArticleAuthor.fullname by requireStringReadonlyProperty("fullname")

val ArticleAuthor.speciality by requireStringReadonlyProperty("speciality")

val ArticleAuthor.rating by requireFloatReadonlyProperty("rating")
