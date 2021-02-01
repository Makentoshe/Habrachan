package com.makentoshe.habrachan.entity.mobiles

import com.google.gson.annotations.SerializedName

data class GetArticlesResponse(
    @SerializedName("articleIds")
    val articleIds: List<Int>,
    @SerializedName("articleRefs")
    val articleRefs: Map<Int, Article>,
    @SerializedName("pagesCount")
    val pagesCount: Int // 50
)
