package com.makentoshe.habrachan.entity.mobile


import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("id")
    val id: String, // 447159
    @SerializedName("title")
    val title: String // Москва
)