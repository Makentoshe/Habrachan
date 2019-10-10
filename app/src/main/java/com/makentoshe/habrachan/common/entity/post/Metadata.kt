package com.makentoshe.habrachan.common.entity.post


import com.google.gson.annotations.SerializedName

data class Metadata(
    @SerializedName("meta_image")
    val metaImage: String
)