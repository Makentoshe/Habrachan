package com.makentoshe.habrachan.common.model.entity


import com.google.gson.annotations.SerializedName

data class GetPostsBySearchResult(
    @SerializedName("data")
    val data: Data,
    @SerializedName("success")
    val success: Boolean
)