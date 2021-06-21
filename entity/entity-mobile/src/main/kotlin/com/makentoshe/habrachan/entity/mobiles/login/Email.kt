package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class Email(
    @SerializedName("errors")
    val errors: List<Any>,
    @SerializedName("ref")
    val ref: Any?, // null
    @SerializedName("value")
    val value: Boolean // true
)