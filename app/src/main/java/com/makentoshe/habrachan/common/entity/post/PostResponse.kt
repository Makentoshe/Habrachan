package com.makentoshe.habrachan.common.entity.post


import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.entity.Article

data class PostResponse(
    @SerializedName("data")
    val article: Article,
    @SerializedName("server_time")
    val serverTime: String
) {

    fun toJson(): String {
        return Gson().toJson(this)
    }

    companion object {
        fun fromJson(json: String): PostResponse {
            return Gson().fromJson(json, PostResponse::class.java)
        }
    }
}