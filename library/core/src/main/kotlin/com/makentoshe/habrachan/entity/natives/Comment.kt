package com.makentoshe.habrachan.entity.natives

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class Comment(
    @SerializedName("id")
    val id: Int,
    @SerializedName("author")
    val author: CommentAuthor,
    @SerializedName("avatar")
    val avatar: String,
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
    val timeChanged: String?,
    @SerializedName("time_published")
    val timePublishedRaw: String

//    @Embedded(prefix = "any_")
//    @SerializedName("vote")
//    val vote: Any?,
) {

    val timePublished: Date
        get() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(timePublishedRaw)
}