package com.makentoshe.habrachan.entity.natives

import com.google.gson.annotations.SerializedName

data class Badge(
    @SerializedName("alias")
    val alias: String, // habred
    @SerializedName("description")
    val description: String, // Пользователь с кармой >0
    @SerializedName("id")
    val id: Int, // 1
    @SerializedName("is_disabled")
    val isDisabled: Boolean, // false
    @SerializedName("is_removable")
    val isRemovable: Boolean, // false
    @SerializedName("title")
    val title: String, // Захабренный
    @SerializedName("url")
    val url: Any? // null
)