package com.makentoshe.habrachan.entity.mobiles

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.ArticleAuthor

data class ArticleAuthor(
    @SerializedName("alias")
    val alias: String, // KD637
    @SerializedName("avatarUrl")
    override val avatar: String, // //habrastorage.org/getpro/habr/avatars/df3/98c/699/df398c699fe20a0144b1913c5f915052.png
    @SerializedName("fullname")
    override val fullname: String?, // null
    @SerializedName("id")
    override val userId: Int, // 2534877
    @SerializedName("login")
    override val login: String, // KD637
    @SerializedName("speciality")
    val speciality: String // officer
): ArticleAuthor