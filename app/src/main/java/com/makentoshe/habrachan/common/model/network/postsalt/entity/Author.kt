package com.makentoshe.habrachan.common.model.network.postsalt.entity


import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.model.entity.Counters
import com.makentoshe.habrachan.common.model.entity.Geo

data class Author(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("badges")
    val badges: List<Any>,
    @SerializedName("common_tags")
    val commonTags: List<Any>,
    @SerializedName("contacts")
    val contacts: List<Any>,
    @SerializedName("counters")
    val counters: Counters,
    @SerializedName("fullname")
    val fullname: Any,
    @SerializedName("geo")
    val geo: Geo,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_can_vote")
    val isCanVote: Boolean,
    @SerializedName("is_rc")
    val isRc: Boolean,
    @SerializedName("is_readonly")
    val isReadonly: Boolean,
    @SerializedName("is_subscribed")
    val isSubscribed: Boolean,
    @SerializedName("login")
    val login: String,
    @SerializedName("path")
    val path: String,
    @SerializedName("rating")
    val rating: Float,
    @SerializedName("rating_position")
    val ratingPosition: Float,
    @SerializedName("score")
    val score: Float,
    @SerializedName("sex")
    val sex: Int,
    @SerializedName("specializm")
    val specializm: Any,
    @SerializedName("time_registered")
    val timeRegistered: String,
    @SerializedName("vote")
    val vote: Int
)