package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class SimilarList(
    @SerializedName("similarListIds")
    val similarListIds: List<Any>,
    @SerializedName("similarListRefs")
    val similarListRefs: Any? // null
)