package com.makentoshe.habrachan.common.model.network.users


import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.model.entity.Data
import com.makentoshe.habrachan.common.model.entity.User

data class GetUsersBySearchResult(
    @SerializedName("data")
    val data: Data<User>,
    @SerializedName("success")
    val success: Boolean
)