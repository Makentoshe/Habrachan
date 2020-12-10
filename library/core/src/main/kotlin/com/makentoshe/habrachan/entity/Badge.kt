package com.makentoshe.habrachan.entity

import com.google.gson.annotations.SerializedName

data class Badge(
    @SerializedName("alias")
    val alias: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_disabled")
    val isDisabled: Boolean,
    @SerializedName("is_removable")
    val isRemovable: Boolean,
    @SerializedName("title")
    val title: String
//    @SerializedName("url")
//    val url: Any
) {

//    fun toJson(): String {
//        return Gson().toJson(this)
//    }
//
//    companion object {
//        fun fromJson(json: String): Badge {
//            return Gson().fromJson(json, Badge::class.java)
//        }
//    }
}