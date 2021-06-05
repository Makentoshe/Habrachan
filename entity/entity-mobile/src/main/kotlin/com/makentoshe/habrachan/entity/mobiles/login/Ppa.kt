package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class Ppa(
    @SerializedName("articles")
    val articles: Any?,
    @SerializedName("card")
    val card: Any?, // null
    @SerializedName("isAccessible")
    val isAccessible: Any?, // null
    @SerializedName("totalTransactions")
    val totalTransactions: Any?, // null
    @SerializedName("transactions")
    val transactions: Any? // null
)