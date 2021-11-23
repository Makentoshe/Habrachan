package com.makentoshe.habrachan.application.common.arena.user.me

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.delegate.*
import com.makentoshe.habrachan.entity.me.mobile.NotificationUnreadCounters
import com.makentoshe.habrachan.entity.me.mobile.ScoreStats
import com.makentoshe.habrachan.entity.me.mobile.Settings
import com.makentoshe.habrachan.entity.user.component.UserAvatar
import com.makentoshe.habrachan.entity.user.component.UserFullName
import com.makentoshe.habrachan.entity.user.component.UserId
import com.makentoshe.habrachan.entity.user.component.UserLogin
import kotlinx.serialization.json.*

data class MeUserFromArena(
    override val parameters: Map<String, JsonElement>,
) : AnyWithVolumeParameters<JsonElement>

val MeUserFromArena.login by requireReadonlyProperty(propertyMapper("alias") { jsonElement ->
    UserLogin(jsonElement.jsonPrimitive.content)
})

val MeUserFromArena.availableInvitesCount by requireReadonlyProperty(intPropertyMapper("availableInvitesCount"))

val MeUserFromArena.avatarUrl by optionReadonlyProperty(propertyMapper("avatarUrl") { jsonElement ->
    return@propertyMapper if (jsonElement is JsonNull) null else UserAvatar(jsonElement.jsonPrimitive.content)
})

val MeUserFromArena.crc32 by requireReadonlyProperty(stringPropertyMapper("crc32"))

val MeUserFromArena.email by requireReadonlyProperty(stringPropertyMapper("email"))

val MeUserFromArena.fullname by requireReadonlyProperty(propertyMapper("fullname") { jsonElement ->
    UserFullName(jsonElement.jsonPrimitive.content)
})

val MeUserFromArena.gaUid by requireReadonlyProperty(stringPropertyMapper("gaUid"))

val MeUserFromArena.id by requireReadonlyProperty(propertyMapper("id") { jsonElement ->
    UserId(jsonElement.jsonPrimitive.intOrNull ?: jsonElement.jsonPrimitive.content.toInt())
})

val MeUserFromArena.rssKey by requireReadonlyProperty(stringPropertyMapper("rssKey"))

val MeUserFromArena.unreadConversationCount by requireReadonlyProperty(stringPropertyMapper("unreadConversationCount"))

val MeUserFromArena.notificationUnreadCounters by requireReadonlyProperty(propertyMapper("notificationUnreadCounters") { jsonElement ->
    NotificationUnreadCounters(jsonElement.jsonObject.toMap())
})

val MeUserFromArena.scoreStats by requireReadonlyProperty(propertyMapper("scoreStats") { jsonElement ->
    ScoreStats(jsonElement.jsonObject.toMap())
})

val MeUserFromArena.settings by requireReadonlyProperty(propertyMapper("settings") { jsonElement ->
    Settings(jsonElement.jsonObject.toMap())
})

val MeUserFromArena.groups by requireReadonlyProperty(listPropertyMapper("groups") { jsonElement ->
    jsonElement.jsonPrimitive.content
})

//val companiesAdmin: List<Any>,

//val groups: List<String>,

//val notices: List<Any>,

//val ppaBalance: Any, // null

//val ppg: Any, // null
