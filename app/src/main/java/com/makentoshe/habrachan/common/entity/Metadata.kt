package com.makentoshe.habrachan.common.entity


import com.google.gson.annotations.SerializedName

data class Metadata(
    @SerializedName("description")
    val description: String,
    @SerializedName("ga_page_type")
    val gaPageType: String,
    @SerializedName("meta_image")
    val metaImage: String
)