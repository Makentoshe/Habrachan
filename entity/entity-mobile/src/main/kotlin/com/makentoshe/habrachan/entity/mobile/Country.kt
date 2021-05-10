package com.makentoshe.habrachan.entity.mobile


import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("id")
    val id: String, // 168
    @SerializedName("title")
    val title: String // Россия
)