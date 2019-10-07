package com.makentoshe.habrachan.common.model.network.postsalt.entity


import com.google.gson.annotations.SerializedName

data class PostsResponse(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("next_page")
    val nextPage: NextPage,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("server_time")
    val serverTime: String,
    @SerializedName("sorted_by")
    val sortedBy: String
)