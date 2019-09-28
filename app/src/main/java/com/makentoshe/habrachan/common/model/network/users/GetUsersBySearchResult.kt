package com.makentoshe.habrachan.common.model.network.users


import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.model.entity.UsersData

data class GetUsersBySearchResult(
    @SerializedName("data")
    val data: UsersData,
    @SerializedName("success")
    val success: Boolean
)