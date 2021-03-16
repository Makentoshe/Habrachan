package com.makentoshe.habrachan.entity.mobiles


import com.google.gson.annotations.SerializedName

data class Workplace(
    @SerializedName("alias")
    val alias: String, // kts
    @SerializedName("title")
    val title: String // KTS
)