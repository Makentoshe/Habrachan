package com.makentoshe.habrachan.entity.mobiles

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.ArticleHub

data class ArticleHub(
    @SerializedName("alias")
    override val alias: String, // skillfactory
    @SerializedName("id")
    override val hubId: Int, // 17931
    @SerializedName("title")
    override val title: String, // SkillFactory corporate blog
    @SerializedName("type")
    val type: String // corporative
): ArticleHub