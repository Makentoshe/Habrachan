package com.makentoshe.habrachan.network

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.natives.PostedComment
import com.makentoshe.habrachan.network.request.PostCommentRequest
import com.makentoshe.habrachan.network.response.PostCommentResponse

data class NativePostCommentResponse(
    override val request: PostCommentRequest,
    override val comment: PostedComment,
) : PostCommentResponse {

    class Factory(
        @SerializedName("comment")
        val comment: PostedComment,
        @SerializedName("ok")
        val ok: Boolean, // true
        @SerializedName("server_time")
        val serverTime: String // 2020-08-01T11:14:14+03:00
    ) {
        fun build(request: PostCommentRequest): NativePostCommentResponse {
            return NativePostCommentResponse(request, comment)
        }
    }
}
