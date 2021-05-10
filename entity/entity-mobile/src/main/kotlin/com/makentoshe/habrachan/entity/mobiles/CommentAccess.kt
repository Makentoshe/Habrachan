package com.makentoshe.habrachan.entity.mobiles


import com.google.gson.annotations.SerializedName

data class CommentAccess(
    @SerializedName("cantCommentReason")
    val cantCommentReason: String, // Authorization required
    @SerializedName("cantCommentReasonKey")
    val cantCommentReasonKey: String, // AUTH_REQUIRED
    @SerializedName("isCanComment")
    val isCanComment: Boolean // false
)