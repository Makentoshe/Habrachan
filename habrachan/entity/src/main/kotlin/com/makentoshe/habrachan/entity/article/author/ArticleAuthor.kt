package com.makentoshe.habrachan.entity.article.author

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.entity.article.author.component.*
import com.makentoshe.habrachan.functional.Option2
import com.makentoshe.habrachan.functional.Require2
import kotlinx.serialization.json.JsonElement

data class ArticleAuthor(
    override val parameters: Map<String, JsonElement>,
    val delegate: ArticleAuthorPropertiesDelegate,
) : AnyWithVolumeParameters<JsonElement>

interface ArticleAuthorPropertiesDelegate {
    val authorId: Require2<AuthorId>
    val authorLogin: Require2<AuthorLogin>
    val authorAvatar: Option2<AuthorAvatar>
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
    override val authorId: Require2<AuthorId>
        get() = Require2(authorId(authorId))

    override val authorLogin: Require2<AuthorLogin>
        get() = Require2(authorLogin(authorLogin))

    override val authorAvatar: Option2<AuthorAvatar>
        get() = Option2.from(authorAvatar?.let(::AuthorAvatar))
})