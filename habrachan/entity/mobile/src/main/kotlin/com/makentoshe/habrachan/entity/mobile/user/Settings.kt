package com.makentoshe.habrachan.entity.mobile.user

data class Settings(
    val chargeSettings: ChargeSettings,
    val langSettings: LangSettings,
    val miscSettings: MiscSettings,
    val permissionSettings: PermissionSettings
)