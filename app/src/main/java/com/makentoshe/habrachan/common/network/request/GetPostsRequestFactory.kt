package com.makentoshe.habrachan.common.network.request

import com.makentoshe.habrachan.common.network.request.GetPostsRequest

data class GetPostsRequestFactory(
    private val client: String,
    private val api: String?,
    private val token: String?
) {
    fun interesting(page: Int): GetPostsRequest {
        require(!(api == null && token == null))
        return GetPostsRequest(
            "posts",
            "interesting",
            client,
            token,
            api,
            page
        )
    }
}