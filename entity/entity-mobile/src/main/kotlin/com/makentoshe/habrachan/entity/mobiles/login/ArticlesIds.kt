package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class ArticlesIds(
    @SerializedName("ARTICLES_LIST_ALL")
    val articlesListAll: List<Int>
)