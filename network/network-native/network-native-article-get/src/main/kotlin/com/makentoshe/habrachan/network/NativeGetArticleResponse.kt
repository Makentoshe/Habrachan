package com.makentoshe.habrachan.network

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.natives.Article
import com.makentoshe.habrachan.network.response.GetArticleResponse2

data class NativeGetArticleResponse(
    override val request: NativeGetArticleRequest,
    override val article: Article,
    val serverTime: String
) : GetArticleResponse2 {

    class Factory(
        @SerializedName("data")
        val article: Article,
        @SerializedName("server_time")
        val serverTime: String
    ) {
        fun build(request: NativeGetArticleRequest): NativeGetArticleResponse {
            return NativeGetArticleResponse(request, article, serverTime)
        }
    }
}