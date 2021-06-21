package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class LangSettings(
    @SerializedName("fl")
    val fl: String, // ru,en
    @SerializedName("hl")
    val hl: String // en
)