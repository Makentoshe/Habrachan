package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class Adblock(
    @SerializedName("hasAcceptableAdsFilter")
    val hasAcceptableAdsFilter: Boolean, // false
    @SerializedName("hasAdblock")
    val hasAdblock: Boolean // false
)