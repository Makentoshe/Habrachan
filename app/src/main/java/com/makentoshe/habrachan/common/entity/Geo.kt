package com.makentoshe.habrachan.common.entity


import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
data class Geo(
    @SerializedName("city")
    val city: String? = null,
    @SerializedName("country")
    val country: String? = null,
    @SerializedName("region")
    val region: String? = null
) {
    val isSpecified: Boolean
        get() = city != null || country != null || region != null
}