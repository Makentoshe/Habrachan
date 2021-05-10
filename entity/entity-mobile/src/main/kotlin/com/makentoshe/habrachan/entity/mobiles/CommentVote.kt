package com.makentoshe.habrachan.entity.mobiles

import com.google.gson.annotations.SerializedName

data class CommentVote(
    @SerializedName("isCanVote")
    val isCanVote: Boolean, // false
    @SerializedName("value")
    val value: Any? // null
)