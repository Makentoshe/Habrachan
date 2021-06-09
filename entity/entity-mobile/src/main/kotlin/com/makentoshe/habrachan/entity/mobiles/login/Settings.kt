package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class Settings(
    @SerializedName("chargeSettings")
    val chargeSettings: ChargeSettings,
    @SerializedName("langSettings")
    val langSettings: LangSettings,
    @SerializedName("miscSettings")
    val miscSettings: MiscSettings,
    @SerializedName("permissionSettings")
    val permissionSettings: PermissionSettings
)