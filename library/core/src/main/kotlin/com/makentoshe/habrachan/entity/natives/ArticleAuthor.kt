package com.makentoshe.habrachan.entity.natives

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.ArticleAuthor
import com.makentoshe.habrachan.entity.natives.Contact
import com.makentoshe.habrachan.entity.natives.Counters
import com.makentoshe.habrachan.entity.natives.Geo
import com.makentoshe.habrachan.entity.natives.Badge

data class ArticleAuthor(
    @SerializedName("avatar")
    override val avatar: String, // https://habr.com/images/avatars/stub-user-middle.gif
    @SerializedName("badges")
    val badges: List<Badge>,
    @SerializedName("common_tags")
    val commonTags: List<Any>,
    @SerializedName("contacts")
    val contacts: List<Contact>,
    @SerializedName("counters")
    val counters: Counters,
    @SerializedName("fullname")
    override val fullname: String, // Хвостов Максим
    @SerializedName("geo")
    val geo: Geo,
    @SerializedName("id")
    override val userId: Int, // 1961555
    @SerializedName("is_rc")
    val isRc: Boolean, // false
    @SerializedName("is_readonly")
    val isReadonly: Boolean, // false
    @SerializedName("is_subscribed")
    val isSubscribed: Boolean, // false
    @SerializedName("login")
    override val login: String, // Makentoshe
    @SerializedName("path")
    val path: String, // /users/makentoshe/
    @SerializedName("payment_methods")
    val paymentMethods: List<Any>,
    @SerializedName("rating")
    val rating: Int, // 0
    @SerializedName("rating_position")
    val ratingPosition: Int, // 0
    @SerializedName("score")
    val score: Int, // 7
    @SerializedName("sex")
    val sex: Int, // 0
    @SerializedName("specializm")
    val specializm: String, // Неполноценный разработчик
    @SerializedName("time_registered")
    val timeRegistered: String // 2019-02-05T23:34:19+03:00
): ArticleAuthor