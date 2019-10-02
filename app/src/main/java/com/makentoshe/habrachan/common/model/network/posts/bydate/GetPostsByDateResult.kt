package com.makentoshe.habrachan.common.model.network.posts.bydate

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.model.entity.Article
import com.makentoshe.habrachan.common.model.entity.Data

data class GetPostsByDateResult(
    @SerializedName("data")
    val data: Data<Article>,
    @SerializedName("success")
    val success: Boolean
)
