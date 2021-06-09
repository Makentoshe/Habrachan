package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class Route(
    @SerializedName("name")
    val name: String, // ARTICLES_LIST_ALL
    @SerializedName("params")
    val params: RouteParams,
    @SerializedName("query")
    val query: Any
)