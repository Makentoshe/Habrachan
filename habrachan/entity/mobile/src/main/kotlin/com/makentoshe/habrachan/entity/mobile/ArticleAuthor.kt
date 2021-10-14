package com.makentoshe.habrachan.entity.mobile

import com.makentoshe.habrachan.delegate.requireFloatReadonlyProperty
import com.makentoshe.habrachan.delegate.requireReadonlyProperty
import com.makentoshe.habrachan.delegate.requireStringReadonlyProperty
import com.makentoshe.habrachan.entity.article.author.ArticleAuthor
import com.makentoshe.habrachan.entity.article.author.ArticleAuthorPropertiesDelegate
import com.makentoshe.habrachan.entity.article.author.component.AuthorAvatar
import com.makentoshe.habrachan.entity.article.author.component.authorId
import com.makentoshe.habrachan.entity.article.author.component.authorLogin
import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.delegate.optionReadonlyProperty
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

fun articleAuthorProperties(
    id: Int,
    login: String,
    avatar: String?,
    fullname: String?,
    speciality: String?,
    rating: Float,
) = mapOf(
    "id" to JsonPrimitive(id),
    "alias" to JsonPrimitive(login),
    "avatar" to JsonPrimitive(avatar),
    "fullname" to JsonPrimitive(fullname),
    "speciality" to JsonPrimitive(speciality),
    "rating" to JsonPrimitive(rating)
)

data class ArticleAuthorPropertiesDelegateImpl(
    override val parameters: Map<String, JsonElement>
) : ArticleAuthorPropertiesDelegate, AnyWithVolumeParameters<JsonElement> {

    override val authorId by requireReadonlyProperty("id") { jsonElement ->
        authorId(jsonElement.jsonPrimitive.int)
    }

    override val authorLogin by requireReadonlyProperty("alias") { jsonElement ->
        authorLogin(jsonElement.jsonPrimitive.content)
    }

    override val authorAvatar by optionReadonlyProperty("avatar") { jsonElement ->
        AuthorAvatar(jsonElement.jsonPrimitive.content)
    }
}

val ArticleAuthor.fullname by requireStringReadonlyProperty("fullname")

val ArticleAuthor.speciality by requireStringReadonlyProperty("speciality")

val ArticleAuthor.rating by requireFloatReadonlyProperty("rating")
