package com.makentoshe.habrachan.common.network.request

data class GetPostRequestFactory(
    private val client: String,
    private val api: String?,
    private val token: String?
) {

    /** Should return a request for receiving a single post by id */
    fun single(id: Int): GetPostRequest {
        require(api != null || token != null)
        return GetPostRequest(client, token, api, id)
    }
}