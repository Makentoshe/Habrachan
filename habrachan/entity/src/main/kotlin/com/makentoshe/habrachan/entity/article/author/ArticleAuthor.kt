package com.makentoshe.habrachan.entity.article.author

import com.makentoshe.habrachan.Option
import com.makentoshe.habrachan.Require
import com.makentoshe.habrachan.entity.article.author.component.*
import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.AnyWithVolumeParameters
import kotlinx.serialization.json.JsonElement

data class ArticleAuthor(
    override val parameters: Map<String, JsonElement>,
    val delegate: ArticleAuthorPropertiesDelegate,
) : AnyWithVolumeParameters<JsonElement>

interface ArticleAuthorPropertiesDelegate {
    val authorId: Require<AuthorId>
    val authorLogin: Require<AuthorLogin>
    val authorAvatar: Option<AuthorAvatar>
}

val ArticleAuthor.authorId get() = delegate.authorId

val ArticleAuthor.authorLogin get() = delegate.authorLogin

val ArticleAuthor.authorAvatar get() = delegate.authorAvatar

fun articleAuthor(
    authorId: Int,
    authorLogin: String,
    authorAvatar: String?,
    parameters: Map<String, JsonElement> = emptyMap(),
) = ArticleAuthor(parameters, object : ArticleAuthorPropertiesDelegate {
    override val authorId: Require<AuthorId>
        get() = Require(authorId(authorId))

    override val authorLogin: Require<AuthorLogin>
        get() = Require(authorLogin(authorLogin))

    override val authorAvatar: Option<AuthorAvatar>
        get() = Option.from(authorAvatar?.let(::AuthorAvatar))
})