package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class Docs(
    @SerializedName("articles")
    val articles: Any,
    @SerializedName("loading")
    val loading: Any,
    @SerializedName("mainMenu")
    val mainMenu: List<Any>,
    @SerializedName("menu")
    val menu: Any
)