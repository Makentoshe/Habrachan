package com.makentoshe.habrachan.common.network.response

import com.google.gson.annotations.SerializedName

sealed class SendCommentResponse {

    class Success(
        @SerializedName("comment")
        val comment: Comment,
        @SerializedName("ok")
        val ok: Boolean,
        @SerializedName("server_time")
        val serverTime: String
    ) : SendCommentResponse() {

        data class Comment(
            @SerializedName("id")
            val id: Int,
            @SerializedName("message")
            val message: String,
            @SerializedName("on_moderated")
            val onModerated: Boolean,
            @SerializedName("time_published")
            val timePublished: String
        )
    }

    class Error(val raw: String): SendCommentResponse()
}
