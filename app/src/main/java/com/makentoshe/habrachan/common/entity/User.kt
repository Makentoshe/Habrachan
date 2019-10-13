package com.makentoshe.habrachan.common.entity


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("badges")
    val badges: List<Badge>,
    @SerializedName("common_tags")
    val commonTags: List<Any>,
    @SerializedName("contacts")
    val contacts: List<Any>,
    @SerializedName("counters")
    val counters: Counters,
    @SerializedName("fullname")
    val fullname: String,
    @SerializedName("geo")
    val geo: Geo,
    @SerializedName("id")
    val id: String,
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
    val ratingPosition: Int,
    @SerializedName("score")
    val score: Float,
    @SerializedName("sex")
    val sex: String,
    @SerializedName("specializm")
    val specializm: String,
    @SerializedName("time_registered")
    val timeRegistered: String,
    @SerializedName("vote")
    val vote: Int = 0,
    @SerializedName("payment_methods")
    val paymentMethods: List<PaymentMethod>?
)