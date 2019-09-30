package com.makentoshe.habrachan.common.model.entity


import com.google.gson.annotations.SerializedName

data class ArticlesData(
    @SerializedName("articles")
    val articles: List<Article>,
    @SerializedName("pages")
    val pages: Int
)