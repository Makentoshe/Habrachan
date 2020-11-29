package com.makentoshe.habrachan.entity

import com.google.gson.annotations.SerializedName

data class CommentAuthor(
    @SerializedName("login")
    val login: String
)