package com.makentoshe.habrachan.entity.natives

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.CommentAuthor

data class CommentAuthor(
    @SerializedName("login")
    override val login: String
) : CommentAuthor