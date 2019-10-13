package com.makentoshe.habrachan.common.model.network.posts

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.model.entity.Data

data class GetPostsResult(
    @SerializedName("data")
    val data: Data<Article>,
    @SerializedName("success")
    val success: Boolean
)