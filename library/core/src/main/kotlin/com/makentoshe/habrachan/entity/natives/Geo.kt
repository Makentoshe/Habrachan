package com.makentoshe.habrachan.entity.natives


import com.google.gson.annotations.SerializedName

data class Geo(
    @SerializedName("city")
    val city: Any?, // null
    @SerializedName("country")
    val country: Any?, // null
    @SerializedName("region")
    val region: Any? // null
)