package com.makentoshe.habrachan.entity

import com.google.gson.annotations.SerializedName

data class NextPage(
    @SerializedName("int")
    val int: Int,
    @SerializedName("url")
    val url: String?
) {

    val isSpecified: Boolean
        get() = url != null
}