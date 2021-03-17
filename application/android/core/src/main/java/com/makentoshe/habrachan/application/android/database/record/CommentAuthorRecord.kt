package com.makentoshe.habrachan.application.android.database.record

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.natives.CommentAuthor

data class CommentAuthorRecord(
    @SerializedName("login")
    val login: String
) {
    constructor(author: CommentAuthor) : this(author.login)

    fun toCommentAuthor() = CommentAuthor(login)
}