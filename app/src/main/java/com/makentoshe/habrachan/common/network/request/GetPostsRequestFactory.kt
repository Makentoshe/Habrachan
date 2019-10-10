package com.makentoshe.habrachan.common.network.request

data class GetPostsRequestFactory(
    private val client: String,
    private val api: String?,
    private val token: String?
) {
    fun interesting(page: Int): GetPostsRequest {
        require(!(api == null && token == null))
        return GetPostsRequest("posts", "interesting", client, token, api, page)
    }

    /**
     * Should return a request saved from previous search.
     * Request should be stored in cache memory on the device
     * and provides in another application runs.
     * If previous request was not stored for some reason - return default request
     */
    fun stored(page: Int): GetPostsRequest {
        // return "default" request
        return interesting(page)
    }
}