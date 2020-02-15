package com.makentoshe.habrachan.common.network.request

data class GetPostsRequest(
    val path1: String,
    val path2: String,
    val client: String,
    val token: String? = null,
    val api: String? = null,
    val page: Int,
    val query: String? = null,
    val sort: String? = null,
    val getArticle: Boolean? = null
) {

    class Builder(private val client: String, private val api: String, private val token: String? = null) {

        fun interesting() = Interesting(client, api, token)

        class Interesting(
            private val client: String, private val api: String, private val token: String? = null
        ) {

            fun build(page: Int) = if (token != null) GetPostsRequest(
                "posts", "interesting", client, token, null, page
            ) else GetPostsRequest(
                "posts", "interesting", client, null, api, page
            )
        }
    }
}

}
