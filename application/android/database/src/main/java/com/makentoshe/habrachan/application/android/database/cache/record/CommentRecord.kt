package com.makentoshe.habrachan.application.android.database.cache.record

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.Comment
import com.makentoshe.habrachan.entity.comment
import com.makentoshe.habrachan.entity.natives.Article

@Entity
data class CommentRecord(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("author")
    @Embedded(prefix = "author_")
    val author: CommentAuthorRecord,
    @SerializedName("avatar")
    val avatar: String?,
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
    @SerializedName("time_changed")
    val timeChanged: String?,
    @SerializedName("time_published")
    val timePublished: String,
    @SerializedName("vote")
    val vote: Int?,

    /** Article id for many comments to 1 article relation. */
    val articleId: Int
) {

    constructor(article: Article, comment: Comment) : this(article.id, comment)

    constructor(articleId: Int, comment: Comment) : this(
        comment.commentId,
        CommentAuthorRecord(comment.author),
        comment.avatar,
        comment.isAuthor,
        comment.isCanVote,
        comment.isFavorite,
        comment.level,
        comment.message,
        comment.parentId,
        comment.score,
        comment.timeChangedRaw,
        comment.timePublishedRaw,
        comment.voteRaw,
        articleId
    )

    fun toComment() = comment(
        id,
        level,
        author.toCommentAuthor(),
        message,
        parentId,
        score,
        avatar,
        isAuthor,
        isCanVote,
        isFavorite,
        timePublished,
        timeChanged,
        vote
    )
}
