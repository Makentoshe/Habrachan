package com.makentoshe.habrachan.entity.mobiles

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("city")
    val city: City,
    @SerializedName("country")
    val country: Country,
    @SerializedName("region")
    val region: Region
)