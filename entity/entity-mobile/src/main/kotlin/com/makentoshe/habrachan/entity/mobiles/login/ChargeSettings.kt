package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class ChargeSettings(
    @SerializedName("commentVoteCount")
    val commentVoteCount: Int, // 18
    @SerializedName("postVoteCount")
    val postVoteCount: Int // 9
)