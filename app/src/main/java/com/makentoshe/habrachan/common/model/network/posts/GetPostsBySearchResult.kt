package com.makentoshe.habrachan.common.model.network.posts

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.model.entity.Data

data class GetPostsBySearchResult(
    @SerializedName("data")
    val data: Data,
    @SerializedName("success")
    val success: Boolean
)