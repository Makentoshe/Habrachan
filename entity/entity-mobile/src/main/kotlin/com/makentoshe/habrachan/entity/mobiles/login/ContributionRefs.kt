package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class ContributionRefs(
    @SerializedName("hubIds")
    val hubIds: Any,
    @SerializedName("hubRefs")
    val hubRefs: Any
)