package com.makentoshe.habrachan.network

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.network.exception.VoteCommentManagerException
import com.makentoshe.habrachan.network.request.VoteCommentRequest2

class NativeVoteCommentException(
    override val request: VoteCommentRequest2,
    override val raw: String,
    val additional: List<String>,
    val code: Int, // 401
    override val message: String,
    override val cause: Throwable? = null,
) : VoteCommentManagerException() {

    class Factory(
        @SerializedName("additional")
        val additional: List<String>,
        @SerializedName("code")
        val code: Int, // 401
        @SerializedName("data")
        val `data`: Any,
        @SerializedName("message")
        val message: String // Authorization required
    ) {
        fun build(request: VoteCommentRequest2, raw: String, throwable: Throwable? = null) = NativeVoteCommentException(
            request, raw, additional, code, message, throwable
        )
    }
}