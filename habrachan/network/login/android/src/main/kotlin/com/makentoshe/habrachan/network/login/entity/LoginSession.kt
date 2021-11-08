package com.makentoshe.habrachan.network.login.entity

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.delegate.requireStringReadonlyProperty
import kotlinx.serialization.json.JsonElement

data class LoginSession(
    override val parameters: Map<String, JsonElement>,
): AnyWithVolumeParameters<JsonElement>

val LoginSession.accessToken by requireStringReadonlyProperty("access_token")

val LoginSession.serverTime by requireStringReadonlyProperty("server_time")
