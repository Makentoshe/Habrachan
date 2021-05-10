package com.makentoshe.habrachan.entity.natives

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.Comment

data class Comment(
    @SerializedName("id")
    override val commentId: Int,
    @SerializedName("author")
    override val author: CommentAuthor,
    @SerializedName("avatar")
    override val avatar: String?,
    @SerializedName("isAuthor")
    override val isAuthor: Boolean,
    @SerializedName("is_can_vote")
    override val isCanVote: Boolean,
    @SerializedName("is_favorite")
    override val isFavorite: Boolean,
    @SerializedName("level")
    override val level: Int,
    @SerializedName("message")
    override val message: String,
    @SerializedName("parent_id")
    override val parentId: Int,
    @SerializedName("score")
    override val score: Int,
    @SerializedName("thread")
    val thread: Int,
    @SerializedName("time_changed")
    override val timeChangedRaw: String?,
    @SerializedName("time_published")
    override val timePublishedRaw: String

//    @Embedded(prefix = "any_")
//    @SerializedName("vote")
//    val vote: Any?,
): Comment