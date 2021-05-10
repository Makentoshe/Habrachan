package com.makentoshe.habrachan.entity.mobiles


import com.google.gson.annotations.SerializedName

data class Region(
    @SerializedName("id")
    val id: String, // 1885
    @SerializedName("title")
    val title: String // Москва и Московская обл.
)