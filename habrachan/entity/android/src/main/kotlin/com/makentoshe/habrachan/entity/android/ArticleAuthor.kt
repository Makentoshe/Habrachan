package com.makentoshe.habrachan.entity.android

import com.makentoshe.habrachan.delegate.requireFloatReadonlyProperty
import com.makentoshe.habrachan.delegate.requireReadonlyProperty
import com.makentoshe.habrachan.entity.article.author.ArticleAuthor
import com.makentoshe.habrachan.entity.article.author.component.authorId
import com.makentoshe.habrachan.entity.article.author.component.authorLogin
import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.delegate.optionReadonlyProperty
import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.delegate.optionStringReadonlyProperty
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

val ArticleAuthor.authorId by requireReadonlyProperty("id") { jsonElement ->
    authorId(jsonElement.jsonPrimitive.int)
}

val ArticleAuthor.authorLogin by requireReadonlyProperty("login") { jsonElement ->
    authorLogin(jsonElement.jsonPrimitive.content)
}

val ArticleAuthor.authorAvatar by optionReadonlyProperty("avatar") { jsonElement ->
    val url = jsonElement.jsonPrimitive.content
    if (url == "https://habr.com/images/avatars/stub-user-middle.gif") null else url
}

val ArticleAuthor.fullname by optionStringReadonlyProperty("fullname")

val ArticleAuthor.speciality by optionStringReadonlyProperty("specializm")

val ArticleAuthor.rating by requireFloatReadonlyProperty("rating")
