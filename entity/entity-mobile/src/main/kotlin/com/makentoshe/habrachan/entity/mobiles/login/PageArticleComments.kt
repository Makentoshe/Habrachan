package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class PageArticleComments(
    @SerializedName("commentRoute")
    val commentRoute: String,
    @SerializedName("freshComments")
    val freshComments: List<Any>,
    @SerializedName("lastCommentTimestamp")
    val lastCommentTimestamp: String,
    @SerializedName("lastViewedComment")
    val lastViewedComment: Int, // 0
    @SerializedName("moderated")
    val moderated: List<Any>,
    @SerializedName("moderatedIds")
    val moderatedIds: List<Any>,
    @SerializedName("postId")
    val postId: Any? // null
)