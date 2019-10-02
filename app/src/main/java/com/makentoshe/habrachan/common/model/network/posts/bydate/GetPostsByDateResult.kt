package com.makentoshe.habrachan.common.model.network.posts.bydate

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.model.entity.ArticlesData

data class GetPostsByDateResult(
    @SerializedName("data")
    val data: ArticlesData,
    @SerializedName("success")
    val success: Boolean
)
