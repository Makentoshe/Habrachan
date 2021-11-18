package com.makentoshe.habrachan.entity.me.mobile

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.delegate.propertyMapper
import com.makentoshe.habrachan.delegate.requireReadonlyProperty
import com.makentoshe.habrachan.entity.mobile.component.toLanguage
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

data class LanguageSettings(
    override val parameters: Map<String, JsonElement>,
): AnyWithVolumeParameters<JsonElement>

val LanguageSettings.contentLanguage by requireReadonlyProperty(propertyMapper("fl") { jsonElement ->
    jsonElement.jsonArray.map { it.jsonPrimitive.content.toLanguage() }
})

val LanguageSettings.habrLanguage by requireReadonlyProperty(propertyMapper("hl") { jsonElement ->
    jsonElement.jsonPrimitive.content.toLanguage()
})