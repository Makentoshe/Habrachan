package com.makentoshe.habrachan.entity.me.mobile

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.delegate.booleanPropertyMapper
import com.makentoshe.habrachan.delegate.requireReadonlyProperty
import kotlinx.serialization.json.JsonElement

data class PermissionSettings(
    override val parameters: Map<String, JsonElement>,
) : AnyWithVolumeParameters<JsonElement>

val PermissionSettings.canAddComplaints by requireReadonlyProperty(booleanPropertyMapper("canAddComplaints"))

val PermissionSettings.canCreateVoices by requireReadonlyProperty(booleanPropertyMapper("canCreateVoices"))
