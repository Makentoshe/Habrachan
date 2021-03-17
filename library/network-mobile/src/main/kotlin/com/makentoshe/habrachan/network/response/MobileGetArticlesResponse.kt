package com.makentoshe.habrachan.network.response

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.mobiles.Article

data class MobileGetArticlesResponse(
    @SerializedName("articleIds")
    val articleIds: List<Int>,
    @SerializedName("articleRefs")
    val articleRefs: Map<Int, Article>,
    @SerializedName("pagesCount")
    val pagesCount: Int // 50
): GetArticlesResponse2 {

    override val articles: List<com.makentoshe.habrachan.entity.Article>
        get() = articleRefs.values.toList()
}
