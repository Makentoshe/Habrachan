package com.makentoshe.habrachan.entity.natives

import com.google.gson.annotations.SerializedName

data class Metadata(
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("ga_page_type")
    val gaPageType: String? = null,
    @SerializedName("meta_image")
    val metaImage: String? = null
)