package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class I18n(
    @SerializedName("fl")
    val fl: String, // ru,en
    @SerializedName("hl")
    val hl: String, // en
    @SerializedName("messages")
    val messages: Map<String, String>
)