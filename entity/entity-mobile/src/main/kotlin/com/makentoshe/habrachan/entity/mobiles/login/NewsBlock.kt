package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class NewsBlock(
    @SerializedName("articleIds")
    val articleIds: Any,
    @SerializedName("articleRefs")
    val articleRefs: Any,
    @SerializedName("requestTime")
    val requestTime: Any
)