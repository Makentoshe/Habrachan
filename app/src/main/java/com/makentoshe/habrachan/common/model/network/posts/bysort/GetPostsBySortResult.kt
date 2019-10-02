package com.makentoshe.habrachan.common.model.network.posts.bysort


import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.model.entity.ArticlesData

data class GetPostsBySortResult(
    @SerializedName("data")
    val data: ArticlesData,
    @SerializedName("success")
    val success: Boolean
)