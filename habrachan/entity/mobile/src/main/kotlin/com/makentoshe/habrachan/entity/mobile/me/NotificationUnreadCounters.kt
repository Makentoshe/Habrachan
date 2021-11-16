package com.makentoshe.habrachan.entity.mobile.me

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.delegate.intPropertyMapper
import com.makentoshe.habrachan.delegate.requireReadonlyProperty
import kotlinx.serialization.json.JsonElement

data class NotificationUnreadCounters(
    override val parameters: Map<String, JsonElement>,
): AnyWithVolumeParameters<JsonElement>

val NotificationUnreadCounters.applications by requireReadonlyProperty(intPropertyMapper("applications"))

val NotificationUnreadCounters.mentions by requireReadonlyProperty(intPropertyMapper("mentions"))

val NotificationUnreadCounters.postsAndComments by requireReadonlyProperty(intPropertyMapper("posts_and_comments"))

val NotificationUnreadCounters.subscribers by requireReadonlyProperty(intPropertyMapper("subscribers"))

val NotificationUnreadCounters.system by requireReadonlyProperty(intPropertyMapper("system"))
