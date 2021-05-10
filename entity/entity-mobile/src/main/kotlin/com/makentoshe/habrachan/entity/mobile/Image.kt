package com.makentoshe.habrachan.entity.mobile

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("fit")
    val fit: String, // cover
    @SerializedName("positionX")
    val positionX: Double, // 0
    @SerializedName("positionY")
    val positionY: Double, // 0
    @SerializedName("url")
    val url: String // https://habrastorage.org/webt/1g/ov/ud/1govudoejlxnbyhxxlqsppcp3te.png
)