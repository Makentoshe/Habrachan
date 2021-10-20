package com.makentoshe.habrachan.network.login

import com.makentoshe.habrachan.AnyWithVolumeParameters
import kotlinx.serialization.json.JsonElement

data class LoginException(
    val request: LoginRequest,
    override val cause: Throwable?,
    override val parameters: Map<String, JsonElement>,
) : Exception(), AnyWithVolumeParameters<JsonElement>