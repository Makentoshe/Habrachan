package com.makentoshe.habrachan.entity

import com.google.gson.annotations.SerializedName

data class PaymentMethod(
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String
)