package com.makentoshe.habrachan.common.model.network.posts.byquery

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.model.entity.ArticlesData

data class GetPostsByQueryResult(
    @SerializedName("data")
    val data: ArticlesData,
    @SerializedName("success")
    val success: Boolean
)