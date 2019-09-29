package com.makentoshe.habrachan.common.model.network.hubs


import com.google.gson.annotations.SerializedName

data class GetHubsNextPage(
    @SerializedName("int")
    val int: Int,
    @SerializedName("url")
    val url: String
)