package com.makentoshe.habrachan.common.model.network.hubs


import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.entity.Hub

data class GetHubsResult(
    @SerializedName("data")
    val data: List<Hub>,
    @SerializedName("next_page")
    val nextPage: GetHubsNextPage,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("server_time")
    val serverTime: String
)