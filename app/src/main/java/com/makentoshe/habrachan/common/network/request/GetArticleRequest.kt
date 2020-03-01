package com.makentoshe.habrachan.common.network.request

data class GetArticleRequest(
    val client: String,
    val api: String,
    val token: String,
    val id: Int
) {
    class Builder(private val client: String, private val token: String, private val api: String) {
        fun single(articleId: Int) = GetArticleRequest(client, token, api, articleId)
    }
}