package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class Sandbox(
    @SerializedName("articleIds")
    val articleIds: List<Any>,
    @SerializedName("articleRefs")
    val articleRefs: Any,
    @SerializedName("isLoading")
    val isLoading: Boolean, // false
    @SerializedName("lastVisitedRoute")
    val lastVisitedRoute: Any,
    @SerializedName("pagesCount")
    val pagesCount: Any?, // null
    @SerializedName("route")
    val route: Any
)