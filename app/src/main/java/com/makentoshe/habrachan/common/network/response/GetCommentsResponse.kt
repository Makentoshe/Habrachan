package com.makentoshe.habrachan.common.network.response

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.entity.comment.Comment

sealed class GetCommentsResponse{

    class Success(
        @SerializedName("data")
        val data: List<Comment>,
        @SerializedName("is_can_comment")
        val isCanComment: Boolean,
        @SerializedName("last")
        val last: Int,
        @SerializedName("server_time")
        val serverTime: String
    ): GetCommentsResponse()

    data class Error(val raw: String): GetCommentsResponse()
}