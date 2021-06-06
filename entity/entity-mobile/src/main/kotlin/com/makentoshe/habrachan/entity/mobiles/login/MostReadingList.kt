package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class MostReadingList(
    @SerializedName("mostReadingListIds")
    val mostReadingListIds: List<Any>,
    @SerializedName("mostReadingListRefs")
    val mostReadingListRefs: Any?, // null
    @SerializedName("promoPost")
    val promoPost: Any? // null
)