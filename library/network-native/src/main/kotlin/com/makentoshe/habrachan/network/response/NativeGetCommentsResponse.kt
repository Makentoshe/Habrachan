package com.makentoshe.habrachan.network.response

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.natives.Comment
import com.makentoshe.habrachan.network.request.NativeGetCommentsRequest

data class NativeGetCommentsResponse(
    override val request: NativeGetCommentsRequest,
    override val data: List<Comment>,
    val isCanComment: Boolean,
    val last: Int,
    val serverTime: String
): GetCommentsResponse {
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
        fun build(request: NativeGetCommentsRequest): NativeGetCommentsResponse {
            return NativeGetCommentsResponse(request, data, isCanComment, last, serverTime)
        }
    }
}
