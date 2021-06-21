package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class DesktopState(
    @SerializedName("desktopFl")
    val desktopFl: Any?, // null
    @SerializedName("desktopHl")
    val desktopHl: Any?, // null
    @SerializedName("isChecked")
    val isChecked: Boolean, // false
    @SerializedName("isLoginDemanded")
    val isLoginDemanded: Boolean // false
)