package com.makentoshe.habrachan.common.entity


import com.google.gson.annotations.SerializedName

data class PaymentMethod(
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
)