package com.makentoshe.habrachan.common.model.network.users


import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.model.entity.Data

data class GetUsersBySearchResult(
    @SerializedName("data")
    val data: Data,
    @SerializedName("success")
    val success: Boolean
)