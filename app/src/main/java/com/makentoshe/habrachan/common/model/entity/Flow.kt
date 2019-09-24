package com.makentoshe.habrachan.common.model.entity


import com.google.gson.annotations.SerializedName

data class Flow(
    @SerializedName("alias")
    val alias: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("path")
    val path: String,
    @SerializedName("url")
    val url: String
)