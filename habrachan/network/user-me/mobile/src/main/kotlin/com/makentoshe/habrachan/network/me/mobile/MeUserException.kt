package com.makentoshe.habrachan.network.me.mobile

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.delegate.optionIntReadonlyProperty
import com.makentoshe.habrachan.delegate.optionStringReadonlyProperty
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

class MeUserException(
    val request: MeUserRequest,
    override val cause: Throwable? = null,
    override val parameters: Map<String, JsonElement> = mapOf()
) : Exception(), AnyWithVolumeParameters<JsonElement>

val MeUserException.networkCode by optionIntReadonlyProperty("code")

val MeUserException.networkMessage by optionStringReadonlyProperty("message")

fun meUserExceptionProperties(networkCode: Int, networkMessage: String) = mapOf(
    "code" to JsonPrimitive(networkCode.toString()),
    "message" to JsonPrimitive(networkMessage)
)