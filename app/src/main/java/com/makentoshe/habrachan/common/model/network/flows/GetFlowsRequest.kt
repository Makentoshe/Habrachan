package com.makentoshe.habrachan.common.model.network.flows

data class GetFlowsRequest(
    val clientKey: String,
    val apiKey: String? = null,
    val accessToken: String? = null
)