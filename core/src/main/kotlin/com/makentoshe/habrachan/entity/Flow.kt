package com.makentoshe.habrachan.entity


import com.google.gson.annotations.SerializedName

data class Flow(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("alias")
    val alias: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("path")
    val path: String,
    @SerializedName("hubs_count")
    val hubsCount: Int?
) {
//
//    fun toJson(): String {
//        return Gson().toJson(this)
//    }
//
//    companion object {
//        fun fromJson(json: String): Flow {
//            return Gson().fromJson(json, Flow::class.java)
//        }
//    }
}