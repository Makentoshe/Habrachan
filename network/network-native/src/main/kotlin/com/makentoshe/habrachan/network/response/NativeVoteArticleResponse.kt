package com.makentoshe.habrachan.network.response

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.network.request.NativeVoteArticleRequest

data class NativeVoteArticleResponse(
    override val request: NativeVoteArticleRequest, override val score: Int, val ok: Boolean, val serverTime: String
) : VoteArticleResponse {

    class Factory(
        @SerializedName("ok")
        val ok: Boolean, // true
        @SerializedName("score")
        val score: Int, // 4
        @SerializedName("server_time")
        val serverTime: String // 2021-05-29T01:11:25+03:00
    ) {
        fun build(request: NativeVoteArticleRequest) = NativeVoteArticleResponse(
            request, score, ok, serverTime
        )
    }
}