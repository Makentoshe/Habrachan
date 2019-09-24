package com.makentoshe.habrachan.common.model.entity


import com.google.gson.annotations.SerializedName

data class Badge(
    @SerializedName("alias")
    val alias: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("is_disabled")
    val isDisabled: Boolean,
    @SerializedName("is_removable")
    val isRemovable: Boolean,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: Any
)