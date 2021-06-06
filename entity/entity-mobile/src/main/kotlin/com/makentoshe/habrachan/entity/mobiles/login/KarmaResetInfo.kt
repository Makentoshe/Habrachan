package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class KarmaResetInfo(
    @SerializedName("canReincarnate")
    val canReincarnate: Any?, // null
    @SerializedName("currentScore")
    val currentScore: Any?, // null
    @SerializedName("wasReincarnated")
    val wasReincarnated: Any? // null
)