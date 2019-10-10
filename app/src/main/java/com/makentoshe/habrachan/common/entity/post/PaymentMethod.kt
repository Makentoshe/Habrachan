package com.makentoshe.habrachan.common.entity.post


import com.google.gson.annotations.SerializedName

data class PaymentMethod(
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
)