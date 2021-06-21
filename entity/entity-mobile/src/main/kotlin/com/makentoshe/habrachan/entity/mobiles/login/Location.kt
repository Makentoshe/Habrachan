package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("searchQuery")
    val searchQuery: Any?, // null
    @SerializedName("urlStruct")
    val urlStruct: UrlStruct
)