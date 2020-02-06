package com.makentoshe.habrachan.common.entity.comment

import com.google.gson.annotations.SerializedName

data class CommentsResponse(
    @SerializedName("data")
    val data: List<Comment>,
    @SerializedName("is_can_comment")
    val isCanComment: Boolean,
    @SerializedName("last")
    val last: Int,
    @SerializedName("server_time")
    val serverTime: String
)