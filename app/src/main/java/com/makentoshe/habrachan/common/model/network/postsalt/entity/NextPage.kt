package com.makentoshe.habrachan.common.model.network.postsalt.entity


import com.google.gson.annotations.SerializedName

data class NextPage(
    @SerializedName("int")
    val int: Int,
    @SerializedName("url")
    val url: String
)