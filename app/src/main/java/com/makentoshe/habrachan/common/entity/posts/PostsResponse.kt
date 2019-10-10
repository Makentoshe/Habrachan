package com.makentoshe.habrachan.common.entity.posts


import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class PostsResponse(
    @SerializedName("data")
    val data: List<Data>,
    @SerializedName("next_page")
    val nextPage: NextPage,
    @SerializedName("pages")
    val pages: Int,
    @SerializedName("server_time")
    val serverTime: String,
    @SerializedName("sorted_by")
    val sortedBy: String
) {

    fun toJson(): String {
        return Gson().toJson(this)
    }

    companion object {
        fun fromJson(json: String): PostsResponse {
            return Gson().fromJson(json, PostsResponse::class.java)
        }
    }
}