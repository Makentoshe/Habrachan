package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class PagesCount(
    @SerializedName("ARTICLES_LIST_ALL")
    val articlesListAll: Int // 50
)