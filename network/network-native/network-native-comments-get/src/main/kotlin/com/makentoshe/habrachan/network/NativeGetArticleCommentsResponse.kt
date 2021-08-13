package com.makentoshe.habrachan.network

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.natives.Comment
import com.makentoshe.habrachan.network.response.GetArticleCommentsResponse

data class NativeGetArticleCommentsResponse(
    override val request: NativeGetArticleCommentsRequest,
    override val data: List<Comment>,
    val isCanComment: Boolean,
    val last: Int,
    val serverTime: String,
) : GetArticleCommentsResponse {

    class Factory(
        @SerializedName("data")
        val data: List<Comment>,
        @SerializedName("is_can_comment")
        val isCanComment: Boolean,
        @SerializedName("last")
        val last: Int,
        @SerializedName("server_time")
        val serverTime: String,
    ) {
        fun build(request: NativeGetArticleCommentsRequest): NativeGetArticleCommentsResponse {
            return NativeGetArticleCommentsResponse(request, data, isCanComment, last, serverTime)
        }
    }
}
