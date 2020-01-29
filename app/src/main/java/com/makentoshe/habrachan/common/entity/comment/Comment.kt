package com.makentoshe.habrachan.common.entity.comment

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("author")
    val author: CommentAuthor,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isAuthor")
    val isAuthor: Boolean,
    @SerializedName("is_can_vote")
    val isCanVote: Boolean,
    @SerializedName("is_favorite")
    val isFavorite: Boolean,
    @SerializedName("level")
    val level: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("parent_id")
    val parentId: Int,
    @SerializedName("score")
    val score: Int,
    @SerializedName("thread")
    val thread: Int,
    @SerializedName("time_changed")
    val timeChanged: String,
    @SerializedName("time_published")
    val timePublished: String,
    @SerializedName("vote")
    val vote: Any
)