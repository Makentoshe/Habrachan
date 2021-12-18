package com.makentoshe.habrachan.delegate

import kotlinx.serialization.json.*

interface JsonElementPropertyMapper<Type> {
    val keys: Array<out String>
    val map: (JsonElement) -> Type?
}

fun <Type> propertyMapper(vararg keys: String, map: (JsonElement) -> Type?) = object : JsonElementPropertyMapper<Type> {
    override val keys: Array<out String> = keys
    override val map: (JsonElement) -> Type? = map
}

fun intPropertyMapper(vararg keys: String) = object: JsonElementPropertyMapper<Int> {
    override val keys: Array<out String> = keys
    override val map: (JsonElement) -> Int? = { jsonElement -> jsonElement.jsonPrimitive.intOrNull }
}

fun stringPropertyMapper(vararg keys: String) = object : JsonElementPropertyMapper<String> {
    override val keys: Array<out String> = keys
    override val map: (JsonElement) -> String? = { jsonElement -> jsonElement.jsonPrimitive.contentOrNull }
}

fun floatPropertyMapper(vararg keys: String) = object : JsonElementPropertyMapper<Float> {
    override val keys: Array<out String> = keys
    override val map: (JsonElement) -> Float? = { jsonElement -> jsonElement.jsonPrimitive.floatOrNull }
}

fun booleanPropertyMapper(vararg keys: String) = object : JsonElementPropertyMapper<Boolean> {
    override val keys: Array<out String> = keys
    override val map: (JsonElement) -> Boolean? = { jsonElement -> jsonElement.jsonPrimitive.booleanOrNull }
}

fun <Type> listPropertyMapper(vararg keys: String, map: (JsonElement) -> Type?) = propertyMapper(*keys) { jsonElement ->
    jsonElement.jsonArray.map(map)
}
