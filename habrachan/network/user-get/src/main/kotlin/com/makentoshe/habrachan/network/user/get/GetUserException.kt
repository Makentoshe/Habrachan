package com.makentoshe.habrachan.network.user.get

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.delegate.optionIntReadonlyProperty
import com.makentoshe.habrachan.delegate.optionStringReadonlyProperty
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

class GetUserException(
    val request: GetUserRequest,
    override val cause: Throwable?,
    override val parameters: Map<String, JsonElement> = mapOf(),
) : AnyWithVolumeParameters<JsonElement>, Exception()

val GetUserException.networkCode by optionIntReadonlyProperty("code")

val GetUserException.networkMessage by optionStringReadonlyProperty("message")

fun getUserExceptionProperties(networkCode: Int, networkMessage: String) = mapOf(
    "code" to JsonPrimitive(networkCode.toString()),
    "message" to JsonPrimitive(networkMessage)
)
