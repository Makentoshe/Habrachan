package com.makentoshe.habrachan.entity.mobiles

import com.google.gson.annotations.SerializedName

data class ArticleBadge(
    @SerializedName("type")
    val type: String,
    @SerializedName("data")
    val data: Any?
)