package com.makentoshe.habrachan.common.entity.comment

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

@Entity
data class Comment(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @Embedded(prefix = "author_")
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
    val timePublished: String,

//    @Embedded(prefix = "any_")
//    @SerializedName("vote")
//    val vote: Any?,

    val articleId: Int
) {
    fun toJson(): String = Gson().toJson(this)

    companion object {
        fun fromJson(json: String): Comment = Gson().fromJson(json, Comment::class.java)
    }
}