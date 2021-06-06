package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class Conversation(
    @SerializedName("isLoadMore")
    val isLoadMore: Boolean, // false
    @SerializedName("messages")
    val messages: List<Any>,
    @SerializedName("respondent")
    val respondent: Any? // null
)