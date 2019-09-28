package com.makentoshe.habrachan.common.model.entity


import com.google.gson.annotations.SerializedName

data class Additional(
    @SerializedName("errors")
    val errors: String
)