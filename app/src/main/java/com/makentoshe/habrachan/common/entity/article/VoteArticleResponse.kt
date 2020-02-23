package com.makentoshe.habrachan.common.entity.article


import com.google.gson.annotations.SerializedName

sealed class VoteArticleResponse {

    class Success(
        @SerializedName("ok")
        val ok: Boolean,
        @SerializedName("score")
        val score: Int,
        @SerializedName("server_time")
        val serverTime: String
    ) : VoteArticleResponse()

    class Error(
        @SerializedName("additional")
        val additional: List<String>,
        @SerializedName("code")
        val code: Int,
        @SerializedName("data")
        val `data`: Any?,
        @SerializedName("message")
        val message: String
    ) : VoteArticleResponse()
}