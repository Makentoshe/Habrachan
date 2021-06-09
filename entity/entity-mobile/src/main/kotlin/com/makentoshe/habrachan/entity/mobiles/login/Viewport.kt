package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class Viewport(
    @SerializedName("prevScrollY")
    val prevScrollY: Any,
    @SerializedName("scrollY")
    val scrollY: Int, // 0
    @SerializedName("width")
    val width: Int // 0
)