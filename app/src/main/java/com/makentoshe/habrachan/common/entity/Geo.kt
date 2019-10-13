package com.makentoshe.habrachan.common.entity


import com.google.gson.annotations.SerializedName

data class Geo(
    @SerializedName("city")
    val city: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("region")
    val region: String
)