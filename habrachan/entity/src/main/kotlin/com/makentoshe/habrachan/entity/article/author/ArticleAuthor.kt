package com.makentoshe.habrachan.entity.article.author

import com.makentoshe.habrachan.Option
import com.makentoshe.habrachan.Require
import com.makentoshe.habrachan.entity.article.author.component.AuthorAvatar
import com.makentoshe.habrachan.entity.article.author.component.AuthorId
import com.makentoshe.habrachan.entity.article.author.component.AuthorLogin
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
