package com.makentoshe.habrachan.entity.mobile.me

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.delegate.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * This is an internal [Me] object that doesn't hold all requireable data
 * that we expected from general Me data class. This data class is valuable
 * only in mobile api. Android api haven't its analog.
 * */
data class InternalMe(
    override val parameters: Map<String, JsonElement>,
) : AnyWithVolumeParameters<JsonElement>

val InternalMe.login by requireReadonlyProperty(stringPropertyMapper("alias"))

val InternalMe.availableInvitesCount by requireReadonlyProperty(intPropertyMapper("availableInvitesCount"))

val InternalMe.avatarUrl by optionReadonlyProperty(stringPropertyMapper("avatarUrl"))

val InternalMe.crc32 by requireReadonlyProperty(stringPropertyMapper("crc32"))

val InternalMe.email by requireReadonlyProperty(stringPropertyMapper("email"))

val InternalMe.fullname by requireReadonlyProperty(stringPropertyMapper("fullname"))

val InternalMe.gaUid by requireReadonlyProperty(stringPropertyMapper("gaUid"))

val InternalMe.id by requireReadonlyProperty(stringPropertyMapper("id"))

val InternalMe.rssKey by requireReadonlyProperty(stringPropertyMapper("rssKey"))

val InternalMe.unreadConversationCount by requireReadonlyProperty(stringPropertyMapper("unreadConversationCount"))

val InternalMe.notificationUnreadCounters by requireReadonlyProperty(propertyMapper("notificationUnreadCounters") { jsonElement ->
    NotificationUnreadCounters(jsonElement.jsonObject.toMap())
})

val InternalMe.scoreStats by requireReadonlyProperty(propertyMapper("scoreStats") { jsonElement ->
    ScoreStats(jsonElement.jsonObject.toMap())
})

val InternalMe.settings by requireReadonlyProperty(propertyMapper("settings") { jsonElement ->
    Settings(jsonElement.jsonObject.toMap())
})

val InternalMe.groups by requireReadonlyProperty(listPropertyMapper("groups") { jsonElement ->
    jsonElement.jsonPrimitive.content
})

//val companiesAdmin: List<Any>,

//val groups: List<String>,

//val notices: List<Any>,

//val ppaBalance: Any, // null

//val ppg: Any, // null
