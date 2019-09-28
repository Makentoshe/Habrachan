package com.makentoshe.habrachan.common.model.network.posts

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.model.entity.ArticlesData

data class GetPostsBySearchResult(
    @SerializedName("data")
    val data: ArticlesData,
    @SerializedName("success")
    val success: Boolean
)