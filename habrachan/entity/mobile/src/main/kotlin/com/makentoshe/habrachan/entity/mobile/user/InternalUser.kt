package com.makentoshe.habrachan.entity.mobile.user

import com.makentoshe.habrachan.AnyWithVolumeParameters
import kotlinx.serialization.json.JsonElement

data class InternalUser(
    override val parameters: Map<String, JsonElement>,
) : AnyWithVolumeParameters<JsonElement>

//val alias: String, // Makentoshe
//val availableInvitesCount: Int, // 0
//val avatarUrl: Any, // null
//val companiesAdmin: List<Any>,
//val crc32: String, // 2612327884
//val email: String, // mkliverout@gmail.com
//val fullname: String, // Хвостов Максим
//val gaUid: String, // 3423668c935b1bf023a407cef909f343
//val groups: List<String>,
//val id: String, // 1961555
//val notices: List<Any>,
//val notificationUnreadCounters: NotificationUnreadCounters,
//val ppaBalance: Any, // null
//val ppg: Any, // null
//val rssKey: String, // 79d71c6c1b51c44e2be8c4bf2e05e4bd
//val scoreStats: ScoreStats,
//val settings: Settings,
//val unreadConversationCount: Int // 0
//