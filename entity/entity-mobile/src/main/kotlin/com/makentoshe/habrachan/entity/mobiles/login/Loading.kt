package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class Loading(
    @SerializedName("article")
    val article: Boolean, // false
    @SerializedName("dropdown")
    val dropdown: Boolean, // false
    @SerializedName("main")
    val main: Boolean // false
)