package com.makentoshe.habrachan.entity.mobiles

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.User
import com.makentoshe.habrachan.entity.mobiles.login.NotificationUnreadCounters
import com.makentoshe.habrachan.entity.mobiles.login.Settings

data class User(
    @SerializedName("alias")
    override val login: String, // Makentoshe
    @SerializedName("avatarUrl")
    val avatarUrl: String?, // null
    @SerializedName("birthday")
    val birthday: String?, // null
    @SerializedName("counterStats")
    val counterStats: CounterStats,
    @SerializedName("followStats")
    val followStats: FollowStats,
    @SerializedName("fullname")
    override val fullname: String, // Хвостов Максим
    @SerializedName("gender")
    val gender: String, // 0
    @SerializedName("isReadonly")
    override val isReadOnly: Boolean, // false
    @SerializedName("lastActivityDateTime")
    val lastActivityDateTime: String, // 2021-03-10T17:46:20+00:00
    @SerializedName("location")
    val location: Location?, // null
    @SerializedName("rating")
    override val rating: Float, // 0
    @SerializedName("ratingPos")
    override val ratingPosition: Int, // 6054
    @SerializedName("registerDateTime")
    override val timeRegisteredRaw: String, // 2019-02-05T20:34:19+00:00
    @SerializedName("relatedData")
    val relatedData: UserRelatedData,
    @SerializedName("scoreStats")
    val scoreStats: ScoreStats,
    @SerializedName("speciality")
    override val speciality: String, // Неполноценный разработчик
    @SerializedName("workplace")
    val workplace: List<Workplace>,

    @SerializedName("availableInvitesCount")
    val availableInvitesCount: Int, // 0
    @SerializedName("companiesAdmin")
    val companiesAdmin: List<Any>,
    @SerializedName("crc32")
    val crc32: String, // 2612327884
    @SerializedName("email")
    val email: String, // mkliverout@gmail.com
    @SerializedName("gaUid")
    val gaUid: String, // 3423668c935b1bf023a407cef909f343
    @SerializedName("groups")
    val groups: List<String>,
    @SerializedName("notices")
    val notices: List<Any>,
    @SerializedName("notificationUnreadCounters")
    val notificationUnreadCounters: NotificationUnreadCounters,
    @SerializedName("ppaBalance")
    val ppaBalance: Any?, // null
    @SerializedName("rssKey")
    val rssKey: String, // 79d71c6c1b51c44e2be8c4bf2e05e4bd
    @SerializedName("settings")
    val settings: Settings,
    @SerializedName("unreadConversationCount")
    val unreadConversationCount: Int // 0
) : User {

    override val avatar: String
        get() = avatarUrl ?: "https://habr.com/images/avatars/stub-user-small.gif"

    override val commentsCount: Int
        get() = counterStats.commentCount

    override val favoritesCount: Int
        get() = counterStats.favoriteCount

    override val postsCount: Int
        get() = counterStats.postCount

    override val followersCount: Int
        get() = followStats.followersCount

    override val followsCount: Int
        get() = followStats.followCount

    override val score: Float
        get() = scoreStats.score

    override val isCanVote: Boolean
        get() = relatedData.canVote

    override val isSubscribed: Boolean
        get() = relatedData.isSubscribed
}