package com.makentoshe.habrachan.entity.android

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.delegate.optionReadonlyProperty
import com.makentoshe.habrachan.delegate.optionStringReadonlyProperty
import com.makentoshe.habrachan.delegate.requireFloatReadonlyProperty
import com.makentoshe.habrachan.delegate.requireReadonlyProperty
import com.makentoshe.habrachan.entity.article.author.ArticleAuthor
import com.makentoshe.habrachan.entity.article.author.ArticleAuthorPropertiesDelegate
import com.makentoshe.habrachan.entity.article.author.component.AuthorAvatar
import com.makentoshe.habrachan.entity.article.author.component.authorId
import com.makentoshe.habrachan.entity.article.author.component.authorLogin
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

data class ArticleAuthorPropertiesDelegateImpl(
    override val parameters: Map<String, JsonElement>
) : ArticleAuthorPropertiesDelegate, AnyWithVolumeParameters<JsonElement> {

    override val authorId by requireReadonlyProperty("id") { jsonElement ->
        authorId(jsonElement.jsonPrimitive.int)
    }

    override val authorLogin by requireReadonlyProperty("login") { jsonElement ->
        authorLogin(jsonElement.jsonPrimitive.content)
    }

    override val authorAvatar by optionReadonlyProperty("avatar") { jsonElement ->
        AuthorAvatar(jsonElement.jsonPrimitive.content)
    }
}

val ArticleAuthor.fullname by optionStringReadonlyProperty("fullname")

val ArticleAuthor.speciality by optionStringReadonlyProperty("specializm")

val ArticleAuthor.rating by requireFloatReadonlyProperty("rating")
