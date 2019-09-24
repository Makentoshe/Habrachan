package com.makentoshe.habrachan.common.model.entity


import com.google.gson.annotations.SerializedName

data class Geo(
    @SerializedName("city")
    val city: Any,
    @SerializedName("country")
    val country: String,
    @SerializedName("region")
    val region: Any
)