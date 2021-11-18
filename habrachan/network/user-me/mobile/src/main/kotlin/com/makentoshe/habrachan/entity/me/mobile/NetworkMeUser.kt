package com.makentoshe.habrachan.entity.me.mobile

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.delegate.*
import com.makentoshe.habrachan.entity.user.component.UserAvatar
import com.makentoshe.habrachan.entity.user.component.UserFullName
import com.makentoshe.habrachan.entity.user.component.UserId
import com.makentoshe.habrachan.entity.user.component.UserLogin
import kotlinx.serialization.json.*

data class NetworkMeUser(
    override val parameters: Map<String, JsonElement>,
) : AnyWithVolumeParameters<JsonElement>

val NetworkMeUser.login by requireReadonlyProperty(propertyMapper("alias") { jsonElement ->
    UserLogin(jsonElement.jsonPrimitive.content)
})

val NetworkMeUser.availableInvitesCount by requireReadonlyProperty(intPropertyMapper("availableInvitesCount"))

val NetworkMeUser.avatarUrl by optionReadonlyProperty(propertyMapper("avatarUrl") { jsonElement ->
    return@propertyMapper if (jsonElement is JsonNull) null else UserAvatar(jsonElement.jsonPrimitive.content)
})

val NetworkMeUser.crc32 by requireReadonlyProperty(stringPropertyMapper("crc32"))

val NetworkMeUser.email by requireReadonlyProperty(stringPropertyMapper("email"))

val NetworkMeUser.fullname by requireReadonlyProperty(propertyMapper("fullname") { jsonElement ->
    UserFullName(jsonElement.jsonPrimitive.content)
})

val NetworkMeUser.gaUid by requireReadonlyProperty(stringPropertyMapper("gaUid"))

val NetworkMeUser.id by requireReadonlyProperty(propertyMapper("id") { jsonElement ->
    UserId(jsonElement.jsonPrimitive.intOrNull ?: jsonElement.jsonPrimitive.content.toInt())
})

val NetworkMeUser.rssKey by requireReadonlyProperty(stringPropertyMapper("rssKey"))

val NetworkMeUser.unreadConversationCount by requireReadonlyProperty(stringPropertyMapper("unreadConversationCount"))

val NetworkMeUser.notificationUnreadCounters by requireReadonlyProperty(propertyMapper("notificationUnreadCounters") { jsonElement ->
    NotificationUnreadCounters(jsonElement.jsonObject.toMap())
})

val NetworkMeUser.scoreStats by requireReadonlyProperty(propertyMapper("scoreStats") { jsonElement ->
    ScoreStats(jsonElement.jsonObject.toMap())
})

val NetworkMeUser.settings by requireReadonlyProperty(propertyMapper("settings") { jsonElement ->
    Settings(jsonElement.jsonObject.toMap())
})

val NetworkMeUser.groups by requireReadonlyProperty(listPropertyMapper("groups") { jsonElement ->
    jsonElement.jsonPrimitive.content
})

//val companiesAdmin: List<Any>,

//val groups: List<String>,

//val notices: List<Any>,

//val ppaBalance: Any, // null

//val ppg: Any, // null
