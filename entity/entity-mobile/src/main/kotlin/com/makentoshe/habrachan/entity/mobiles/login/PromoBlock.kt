package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class PromoBlock(
    @SerializedName("promoItems")
    val promoItems: List<Any>
)