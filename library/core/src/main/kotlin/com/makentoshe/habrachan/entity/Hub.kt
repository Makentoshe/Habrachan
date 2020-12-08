package com.makentoshe.habrachan.entity

import com.google.gson.annotations.SerializedName

data class Hub(
    @SerializedName("id")
    val id: Int,
    @SerializedName("alias")
    val alias: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("is_company")
    val isCompany: Boolean,
    @SerializedName("is_membership")
    val isMembership: Boolean,
    @SerializedName("is_profiled")
    val isProfiled: Boolean,
    @SerializedName("path")
    val path: String,
    @SerializedName("about")
    val about: String?,
    @SerializedName("about_small")
    val aboutSmall: String?,
    @SerializedName("count_posts")
    val countPosts: Int?,
    @SerializedName("count_subscribers")
    val countSubscribers: Int?,
    @SerializedName("flow")
    val flow: Flow?,
    @SerializedName("rating")
    val rating: Double?,
    @SerializedName("tags_string")
    val tagsString: String?
) {

//    fun toJson(): String {
//        return Gson().toJson(this)
//    }
//
//    companion object {
//        fun fromJson(json: String): Hub {
//            return Gson().fromJson(json, Hub::class.java)
//        }
//    }
}