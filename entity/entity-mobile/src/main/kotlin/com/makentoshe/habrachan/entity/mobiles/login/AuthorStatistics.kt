package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class AuthorStatistics(
    @SerializedName("articleIds")
    val articleIds: Any,
    @SerializedName("articleRefs")
    val articleRefs: Any,
    @SerializedName("maxStatsCount")
    val maxStatsCount: Any,
    @SerializedName("pagesCount")
    val pagesCount: Any,
    @SerializedName("route")
    val route: Any,
    @SerializedName("viewsCount")
    val viewsCount: List<Any>
)