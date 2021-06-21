package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class Hubs(
    @SerializedName("hubIds")
    val hubIds: Any,
    @SerializedName("hubRefs")
    val hubRefs: Any,
    @SerializedName("isLoading")
    val isLoading: Boolean, // false
    @SerializedName("pagesCount")
    val pagesCount: Any,
    @SerializedName("route")
    val route: Any
)