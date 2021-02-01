package com.makentoshe.habrachan.mobiles.network.response

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.mobiles.Article

data class GetArticlesResponse(
    @SerializedName("articleIds")
    val articleIds: List<Int>,
    @SerializedName("articleRefs")
    val articleRefs: Map<Int, Article>,
    @SerializedName("pagesCount")
    val pagesCount: Int // 50
)
