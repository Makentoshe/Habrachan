package com.makentoshe.habrachan.entity.mobile

import com.google.gson.annotations.SerializedName

data class CommentVote(
    @SerializedName("isCanVote")
    val isCanVote: Boolean, // false
    @SerializedName("value")
    val value: Any? // null
)