package com.makentoshe.habrachan.common.model.network.flows

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.model.entity.Flow

data class GetFlowsResult(
    @SerializedName("data")
    val data: List<Flow>,
    @SerializedName("server_time")
    val serverTime: String
)