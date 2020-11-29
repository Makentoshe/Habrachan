package com.makentoshe.habrachan.network.response

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.Comment

data class GetCommentsResponse(
    @SerializedName("data")
    val data: List<Comment>,
    @SerializedName("is_can_comment")
    val isCanComment: Boolean,
    @SerializedName("last")
    val last: Int,
    @SerializedName("server_time")
    val serverTime: String
)