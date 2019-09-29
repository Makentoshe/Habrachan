package com.makentoshe.habrachan.common.model.network.hubs

data class GetHubsRequest(
    val clientKey: String,
    val apiKey: String? = null,
    val accessToken: String? = null,
    val hubAlias: String,
    val page: Int
)