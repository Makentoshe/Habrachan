package com.makentoshe.habrachan.entity.mobiles


import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("id")
    val id: String, // 168
    @SerializedName("title")
    val title: String // Россия
)