package com.makentoshe.habrachan.common.model.network.postsalt

data class GetPostsRequestFactory(
    private val client: String,
    private val api: String?,
    private val token: String?
) {
    fun interesting(page: Int): GetPostsRequest {
        require(!(api == null && token == null))
        return GetPostsRequest("posts", "interesting", client, token, api, page)
    }
}