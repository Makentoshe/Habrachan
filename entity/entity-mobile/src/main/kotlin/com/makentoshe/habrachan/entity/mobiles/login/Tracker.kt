package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class Tracker(
    @SerializedName("items")
    val items: Any,
    @SerializedName("markedReadSilently")
    val markedReadSilently: Any,
    @SerializedName("pagesCache")
    val pagesCache: Any,
    @SerializedName("unreadCounters")
    val unreadCounters: UnreadCounters
)