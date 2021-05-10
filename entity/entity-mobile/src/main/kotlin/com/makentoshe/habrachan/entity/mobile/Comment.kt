package com.makentoshe.habrachan.entity.mobile

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.Comment

data class Comment(
    @SerializedName("id")
    override val commentId: Int, // 19831270
    @SerializedName("author")
    override val author: CommentAuthor,
    @SerializedName("children")
    val children: List<Int>,
    @SerializedName("editorVersion")
    val editorVersion: Int, // 1
    @SerializedName("isAuthor")
    override val isAuthor: Boolean, // false
    @SerializedName("isCanEdit")
    val isCanEdit: Boolean, // false
    @SerializedName("isFavorite")
    override val isFavorite: Boolean, // false
    @SerializedName("isNew")
    val isNew: Boolean, // false
    @SerializedName("isPostAuthor")
    val isPostAuthor: Boolean, // false
    @SerializedName("isSuspended")
    val isSuspended: Boolean, // false
    @SerializedName("level")
    override val level: Int, // 0
    @SerializedName("message")
    override val message: String, // <div xmlns="http://www.w3.org/1999/xhtml">По мне anko неудобно по причине отсутствия адекватного превью, который будет нормально работать и без билда. Так все супер, но это большой недостаток, который мешает просто пипец как работать<br/></div>
    @SerializedName("parentId")
    override val parentId: Int, // null
    @SerializedName("score")
    override val score: Int, // 1
    @SerializedName("timeChanged")
    override val timeChangedRaw: String?, // 2019-03-03T15:03:06+00:00
    @SerializedName("timeEditAllowedTill")
    val timeEditAllowedTill: Any?, // null
    @SerializedName("timePublished")
    override val timePublishedRaw: String, // 2019-03-03T14:56:56+00:00
    @SerializedName("vote")
    val vote: CommentVote,
    @SerializedName("votesCount")
    val votesCount: Int // 1
) : Comment {

    override val isCanVote: Boolean
        get() = vote.isCanVote

    override val avatar: String?
        get() = author.avatarUrl
}