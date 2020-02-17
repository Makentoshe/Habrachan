package com.makentoshe.habrachan.common.network.request

data class GetArticlesRequest(
    val path1: String,
    val path2: String,
    val client: String,
    val token: String?,
    val api: String?,
    val page: Int,
    val query: String?,
    val sort: String?,
    val getArticle: Boolean?
) {

    class Builder(private val client: String, private val api: String, private val token: String? = null) {

        var query: String? = null
        var sort: String? = null
        var getArticle: Boolean? = null

        fun interesting(page: Int) = default("posts", "interesting", page)

        fun all(page: Int) = default("posts", "all", page)

        fun feed(page: Int) = default("feed", "all", page)

        private fun default(path1: String, path2: String, page: Int) = if (token != null) GetArticlesRequest(
            path1, path2, client, token, null, page, query, sort, getArticle
        ) else GetArticlesRequest(
            path1, path2, client, null, api, page, query, sort, getArticle
        )
    }
}
