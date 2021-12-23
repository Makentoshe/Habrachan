package com.makentoshe.habrachan.network.content.get

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.delegate.optionIntReadonlyProperty
import com.makentoshe.habrachan.delegate.optionStringReadonlyProperty
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

class GetContentException(
    val request: GetContentRequest,
    override val cause: Throwable?,
    override val parameters: Map<String, JsonElement> = mapOf(),
) : AnyWithVolumeParameters<JsonElement>, Exception()

val GetContentException.networkCode by optionIntReadonlyProperty("code")

val GetContentException.networkMessage by optionStringReadonlyProperty("message")

fun getContentExceptionProperties(networkCode: Int, networkMessage: String) = mapOf(
    "code" to JsonPrimitive(networkCode.toString()),
    "message" to JsonPrimitive(networkMessage)
)
