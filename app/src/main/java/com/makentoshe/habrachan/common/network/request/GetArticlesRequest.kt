package com.makentoshe.habrachan.common.network.request

data class GetArticlesRequest(
    val client: String,
    val api: String,
    val token: String,
    val page: Int,
    val spec: String
)  {

    class Factory(private val client: String, private val api: String, private val token: String) {
        fun all(page: Int) = GetArticlesRequest(client, api, token, page, "posts/all")
    }
}