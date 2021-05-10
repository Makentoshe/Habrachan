package com.makentoshe.habrachan.application.android.database.record

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.CommentAuthor
import com.makentoshe.habrachan.entity.commentAuthor

data class CommentAuthorRecord(
    @SerializedName("login")
    val login: String
) {
    constructor(author: CommentAuthor) : this(author.login)

    fun toCommentAuthor() = commentAuthor(login)
}