package com.makentoshe.habrachan.common.model.entity


import com.google.gson.annotations.SerializedName

data class Data<T>(
    @SerializedName(value = "article", alternate = ["users", "articles"])
    val data: List<T>,
    @SerializedName("pages")
    val pages: Int
)