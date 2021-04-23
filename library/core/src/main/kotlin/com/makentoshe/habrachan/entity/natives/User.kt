package com.makentoshe.habrachan.entity.natives


import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.User
import com.makentoshe.habrachan.entity.UserId

data class User(
    @SerializedName("avatar")
    override val avatar: String, // https://habr.com/images/avatars/stub-user-middle.gif
    @SerializedName("badges")
    val badges: List<Badge>,
    @SerializedName("common_tags")
    val commonTags: List<Any>,
    @SerializedName("contacts")
    val contacts: List<Contact>, // may be empty
    @SerializedName("counters")
    val counters: Counters,
    @SerializedName("fullname")
    override val fullname: String, // Хвостов Максим
    @SerializedName("geo")
    val geo: Geo,
    @SerializedName("id")
    override val userId: Int, // 1961555
    @SerializedName("is_can_vote")
    override val isCanVote: Boolean, // false
    @SerializedName("is_rc")
    val isRc: Boolean, // false
    @SerializedName("is_readonly")
    override val isReadOnly: Boolean, // false
    @SerializedName("is_subscribed")
    override val isSubscribed: Boolean, // false
    @SerializedName("login")
    override val login: String, // Makentoshe
    @SerializedName("path")
    val path: String, // /users/makentoshe/
    @SerializedName("rating")
    override val rating: Float, // 0
    @SerializedName("rating_position")
    override val ratingPosition: Int, // 0
    @SerializedName("score")
    override val score: Float, // 7
    @SerializedName("sex")
    val sex: Int, // 0
    @SerializedName("specializm")
    override val speciality: String, // Неполноценный разработчик
    @SerializedName("time_registered")
    override val timeRegisteredRaw: String // 2019-02-05T23:34:19+03:00
): User, UserId {

    override val commentsCount: Int
        get() = counters.comments

    override val favoritesCount: Int
        get() = counters.favorites

    override val followersCount: Int
        get() = counters.followers

    override val followsCount: Int
        get() = counters.followed

    override val postsCount: Int
        get() = counters.posts
}

// TODO define properties

//    @SerializedName("vote")
//    val vote: Int = 0
//    @SerializedName("payment_methods")
//    val paymentMethods: List<PaymentMethod>?,
//    @SerializedName("common_tags")
//    val commonTags: List<Any>,
//    @SerializedName("contacts")
//    val contacts: List<Any>,
