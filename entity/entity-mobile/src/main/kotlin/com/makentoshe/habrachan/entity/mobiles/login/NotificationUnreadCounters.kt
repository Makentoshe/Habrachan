package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class NotificationUnreadCounters(
    @SerializedName("applications")
    val applications: Int, // 0
    @SerializedName("mentions")
    val mentions: Int, // 0
    @SerializedName("posts_and_comments")
    val postsAndComments: Int, // 1
    @SerializedName("subscribers")
    val subscribers: Int, // 0
    @SerializedName("system")
    val system: Int // 0
)