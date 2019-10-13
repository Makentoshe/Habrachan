package com.makentoshe.habrachan.common.model.network.posts.bysort


import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.model.entity.Data

data class GetPostsBySortResult(
    @SerializedName("data")
    val data: Data<Article>,
    @SerializedName("success")
    val success: Boolean
)