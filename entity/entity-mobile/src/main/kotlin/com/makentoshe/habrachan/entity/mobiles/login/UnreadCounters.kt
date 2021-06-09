package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class UnreadCounters(
    @SerializedName("applications")
    val applications: Any?, // null
    @SerializedName("mentions")
    val mentions: Any?, // null
    @SerializedName("posts_and_comments")
    val postsAndComments: Any?, // null
    @SerializedName("subscribers")
    val subscribers: Any?, // null
    @SerializedName("system")
    val system: Any? // null
)