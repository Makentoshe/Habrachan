package com.makentoshe.habrachan.common.entity


import com.google.gson.annotations.SerializedName

data class Geo(
    @SerializedName("city")
    val city: String? = null,
    @SerializedName("country")
    val country: String? = null,
    @SerializedName("region")
    val region: String? = null
)