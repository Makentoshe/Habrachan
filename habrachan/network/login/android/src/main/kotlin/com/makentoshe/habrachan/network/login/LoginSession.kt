package com.makentoshe.habrachan.network.login

import com.makentoshe.habrachan.AnyWithVolumeParameters
import kotlinx.serialization.json.JsonElement

data class LoginSession(
    override val parameters: Map<String, JsonElement>
): AnyWithVolumeParameters<JsonElement>