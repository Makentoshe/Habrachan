package com.makentoshe.habrachan.network.exception


import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("field")
    val `field`: String, // email
    @SerializedName("key")
    val key: String // MISSING
)