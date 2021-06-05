package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class Global(
    @SerializedName("device")
    val device: String, // desktop
    @SerializedName("isPwa")
    val isPwa: Boolean // false
)