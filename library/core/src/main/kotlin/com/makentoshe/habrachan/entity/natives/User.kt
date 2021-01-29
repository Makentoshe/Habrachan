package com.makentoshe.habrachan.entity.natives

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.ArticleAuthor

data class User(
    @SerializedName("avatar")
    override val avatar: String,
    @SerializedName("badges")
    val badges: List<Badge>,
    @SerializedName("counters")
    val counters: Counters,
    @SerializedName("fullname")
    override val fullname: String? = null,
    @SerializedName("geo")
    val geo: Geo? = null,
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_can_vote")
    val isCanVote: Boolean,
    @SerializedName("is_rc")
    val isRc: Boolean,
    @SerializedName("is_readonly")
    val isReadonly: Boolean,
    @SerializedName("is_subscribed")
    val isSubscribed: Boolean? = null,
    @SerializedName("login")
    override val login: String,
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
    val specializm: String? = null,
    @SerializedName("time_registered")
    val timeRegistered: String
//    @SerializedName("vote")
//    val vote: Int = 0
//    @SerializedName("payment_methods")
//    val paymentMethods: List<PaymentMethod>?,
//    @SerializedName("common_tags")
//    val commonTags: List<Any>,
//    @SerializedName("contacts")
//    val contacts: List<Any>,
): ArticleAuthor {
    override val userId: Int
        get() = id
}