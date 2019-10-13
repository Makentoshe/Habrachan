package com.makentoshe.habrachan.common.entity.post


import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.entity.Data

data class PostResponse(
    @SerializedName("data")
    val data: Data,
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