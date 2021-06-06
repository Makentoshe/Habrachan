package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class Ssr(
    @SerializedName("error")
    val error: Any?, // null
    @SerializedName("isDataLoaded")
    val isDataLoaded: Boolean, // false
    @SerializedName("isDataLoading")
    val isDataLoading: Boolean, // false
    @SerializedName("isHydrationFailed")
    val isHydrationFailed: Boolean, // false
    @SerializedName("isI18nLoaded")
    val isI18nLoaded: Boolean, // true
    @SerializedName("isServer")
    val isServer: Boolean // false
)