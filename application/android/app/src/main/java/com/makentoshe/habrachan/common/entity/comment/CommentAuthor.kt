package com.makentoshe.habrachan.common.entity.comment

import com.google.gson.annotations.SerializedName

data class CommentAuthor(
    @SerializedName("login")
    val login: String
)