package com.makentoshe.habrachan.common.network.request

data class GetCommentsRequest(
    val client: String,
    val token: String,
    val api: String?,
    val articleId: Int,
    val since: Int = -1
) {
    class Factory(
        private val client: String,
        private val api: String,
        private val token: String
    ) {
        fun build(articleId: Int, since: Int = -1) : GetCommentsRequest {
            return GetCommentsRequest(client, token, api, articleId, since)
        }
    }
}