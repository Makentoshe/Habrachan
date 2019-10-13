package com.makentoshe.habrachan.common.model.network.posts.byquery

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.model.entity.Data

data class GetPostsByQueryResult(
    @SerializedName("data")
    val data: Data<Article>,
    @SerializedName("success")
    val success: Boolean
)