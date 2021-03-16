package com.makentoshe.habrachan.entity.mobiles


import com.google.gson.annotations.SerializedName

data class City(
    @SerializedName("id")
    val id: String, // 447159
    @SerializedName("title")
    val title: String // Москва
)