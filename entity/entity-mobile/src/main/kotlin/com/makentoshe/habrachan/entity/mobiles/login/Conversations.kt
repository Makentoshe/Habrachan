package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class Conversations(
    @SerializedName("conversations")
    val conversations: List<Any>,
    @SerializedName("isLoadMore")
    val isLoadMore: Boolean, // false
    @SerializedName("pagesCount")
    val pagesCount: Int, // 0
    @SerializedName("unreadCount")
    val unreadCount: Int // 0
)