package com.makentoshe.habrachan.entity.me.mobile

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.delegate.propertyMapper
import com.makentoshe.habrachan.delegate.requireReadonlyProperty
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

data class Settings(
    override val parameters: Map<String, JsonElement>,
) : AnyWithVolumeParameters<JsonElement>

val Settings.languageSettings by requireReadonlyProperty(propertyMapper("langSettings") { jsonElement ->
    LanguageSettings(jsonElement.jsonObject.toMap())
})

val Settings.chargeSettings by requireReadonlyProperty(propertyMapper("chargeSettings") { jsonElement ->
    ChargeSettings(jsonElement.jsonObject.toMap())
})

val Settings.miscSettings by requireReadonlyProperty(propertyMapper("miscSettings") { jsonElement ->
    MiscSettings(jsonElement.jsonObject.toMap())
})

val Settings.permissionSettings by requireReadonlyProperty(propertyMapper("permissionSettings") { jsonElement ->
    PermissionSettings(jsonElement.jsonObject.toMap())
})