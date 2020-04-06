package com.makentoshe.habrachan.common.entity.comment

import com.google.gson.annotations.SerializedName

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

    class Error(
        @SerializedName("code")
        val code: Int,
        @SerializedName("message")
        val message: String,
        @SerializedName("additional")
        val additional: Array<String>
    ): GetCommentsResponse()
}