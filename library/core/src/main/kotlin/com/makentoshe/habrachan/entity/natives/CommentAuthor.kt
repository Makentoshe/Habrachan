package com.makentoshe.habrachan.entity.natives

import com.google.gson.annotations.SerializedName

data class CommentAuthor(
    @SerializedName("login")
    val login: String
)