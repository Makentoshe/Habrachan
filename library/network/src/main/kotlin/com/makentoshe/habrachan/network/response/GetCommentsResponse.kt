package com.makentoshe.habrachan.network.response

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.natives.Comment
import com.makentoshe.habrachan.network.request.GetCommentsRequest

data class GetCommentsResponse(
    val request: GetCommentsRequest,
    val data: List<Comment>,
    val isCanComment: Boolean,
    val last: Int,
    val serverTime: String
) {
    class Factory(
        @SerializedName("data")
        val data: List<Comment>,
        @SerializedName("is_can_comment")
        val isCanComment: Boolean,
        @SerializedName("last")
        val last: Int,
        @SerializedName("server_time")
        val serverTime: String
    ) {
        fun get(request: GetCommentsRequest): GetCommentsResponse {
            return GetCommentsResponse(request, data, isCanComment, last, serverTime)
        }
    }
}