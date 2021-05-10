package com.makentoshe.habrachan.entity.mobiles


import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.CommentAuthor

data class CommentAuthor(
    @SerializedName("alias")
    override val login: String, // androidovshchik
    @SerializedName("avatarUrl")
    val avatarUrl: String?, // null
    @SerializedName("fullname")
    val fullname: String, // Влад
    @SerializedName("id")
    val id: String, // 1103690
    @SerializedName("speciality")
    val speciality: Any? // null
): CommentAuthor