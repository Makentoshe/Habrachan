package com.makentoshe.habrachan.network.exception

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.network.request.VoteArticleRequest

data class NativeVoteArticleException(
    override val request: VoteArticleRequest,
    override val raw: String,
    val additional: List<String>,
    val code: Int, // 401
    override val message: String,
    override val cause: Throwable? = null,
) : VoteArticleDeserializerException() {

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
        fun build(request: VoteArticleRequest, raw: String, throwable: Throwable? = null) = NativeVoteArticleException(
            request, raw, additional, code, message, throwable
        )
    }
}
