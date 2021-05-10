package com.makentoshe.habrachan.entity.mobile

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.ArticleFlow

data class ArticleFlow(
    @SerializedName("alias")
    override val alias: String, // develop
    @SerializedName("id")
    override val flowId: Int, // 1
    @SerializedName("title")
    override val title: String // Development
): ArticleFlow