package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class Info(
    @SerializedName("infoPage")
    val infoPage: Any,
    @SerializedName("isLoading")
    val isLoading: Boolean // true
)