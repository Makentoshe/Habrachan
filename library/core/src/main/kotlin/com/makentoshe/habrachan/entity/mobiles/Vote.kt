package com.makentoshe.habrachan.entity.mobiles

import com.google.gson.annotations.SerializedName

data class Vote(
    @SerializedName("value")
    val value: Any?, // null
    @SerializedName("voteTimeExpired")
    val voteTimeExpired: String // 2021-02-27T11:06:43+00:00
)