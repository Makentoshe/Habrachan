package com.makentoshe.habrachan.common.model.entity


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("articles")
    val articles: List<Article>?,
    @SerializedName("code")
    val code: Int = 200,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: Any?,
    @SerializedName("pages")
    val pages: Int?
)