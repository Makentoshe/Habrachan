package com.makentoshe.habrachan.entity.me.mobile

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.delegate.booleanPropertyMapper
import com.makentoshe.habrachan.delegate.requireReadonlyProperty
import kotlinx.serialization.json.JsonElement

data class MiscSettings(
    override val parameters: Map<String, JsonElement>,
): AnyWithVolumeParameters<JsonElement>

val MiscSettings.hideAdv by requireReadonlyProperty(booleanPropertyMapper("hideAdv"))

val MiscSettings.viewCommentsRefresh by requireReadonlyProperty(booleanPropertyMapper("viewCommentsRefresh"))

val MiscSettings.enableShortcuts by requireReadonlyProperty(booleanPropertyMapper("enableShortcuts"))

// val digestSubscription: Any, // null
