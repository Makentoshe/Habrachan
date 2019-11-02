package com.makentoshe.habrachan.common.entity


import com.google.gson.annotations.SerializedName

data class Metadata(
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("ga_page_type")
    val gaPageType: String? = null,
    @SerializedName("meta_image")
    val metaImage: String? = null
)