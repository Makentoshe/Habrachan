package com.makentoshe.habrachan.network.login

import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.api.login.LoginAuth
import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.AnyWithVolumeParameters
import kotlinx.serialization.json.JsonElement

data class LoginRequest(val parameters: AdditionalRequestParameters, val loginAuth: LoginAuth)

data class LoginSession(
    override val parameters: Map<String, JsonElement>
): AnyWithVolumeParameters<JsonElement>