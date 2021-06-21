package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class MiscSettings(
    @SerializedName("digestSubscription")
    val digestSubscription: Any?, // null
    @SerializedName("enableShortcuts")
    val enableShortcuts: Boolean, // true
    @SerializedName("hideAdv")
    val hideAdv: Boolean, // true
    @SerializedName("viewCommentsRefresh")
    val viewCommentsRefresh: Boolean // true
)