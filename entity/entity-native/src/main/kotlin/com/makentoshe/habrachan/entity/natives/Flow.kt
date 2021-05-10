package com.makentoshe.habrachan.entity.natives


import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.ArticleFlow

data class Flow(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("alias")
    override val alias: String,
    @SerializedName("url")
    val url: String?,
    @SerializedName("path")
    val path: String?,
    @SerializedName("hubs_count")
    val hubsCount: Int?
) : ArticleFlow {

    override val title: String
        get() = name

    override val flowId: Int
        get() = id
}