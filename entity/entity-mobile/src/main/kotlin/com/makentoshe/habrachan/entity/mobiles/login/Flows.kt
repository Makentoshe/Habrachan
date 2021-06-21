package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.mobiles.ArticleFlow

data class Flows(
    @SerializedName("flows")
    val flows: List<ArticleFlow>
)