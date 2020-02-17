package com.makentoshe.habrachan.common.network.request

data class GetArticleRequest(
    val client: String,
    val token: String?,
    val api: String?,
    val id: Int
) {
    class Builder(private val client: String, private val api: String, private val token: String?) {
        fun single(articleId: Int) = GetArticleRequest(client, token, api, articleId)
    }
}