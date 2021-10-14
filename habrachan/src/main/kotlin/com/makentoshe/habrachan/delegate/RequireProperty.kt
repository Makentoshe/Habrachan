package com.makentoshe.habrachan.delegate

import com.makentoshe.habrachan.Require
import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.AnyWithVolumeParameters
import kotlinx.serialization.json.*
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class RequireReadonlyProperty<Owner, Type>: ReadOnlyProperty<Owner, Require<Type>>

abstract class RequireReadWriteProperty<Owner, Type>: ReadWriteProperty<Owner, Require<Type>>

class JsonElementRequireReadonlyProperty<Type>(
    private vararg val keys: String,
    private val map: (JsonElement) -> Type
) : RequireReadonlyProperty<AnyWithVolumeParameters<JsonElement>, Type>() {

    override fun getValue(thisRef: AnyWithVolumeParameters<JsonElement>, property: KProperty<*>): Require<Type> {
        val initial = thisRef.parameters[keys.first()]
        return Require(keys.drop(1).fold(initial) { jsonElement, key -> jsonElement!!.jsonObject[key] }?.let(map))
    }

}

fun <Type> requireReadonlyProperty(vararg keys: String, map: (JsonElement) -> Type): JsonElementRequireReadonlyProperty<Type> {
    return JsonElementRequireReadonlyProperty(*keys, map = map)
}

fun requireStringReadonlyProperty(vararg keys: String): JsonElementRequireReadonlyProperty<String> {
    return JsonElementRequireReadonlyProperty(*keys) { jsonElement -> jsonElement.jsonPrimitive.content }
}

fun requireBooleanReadonlyProperty(vararg keys: String): JsonElementRequireReadonlyProperty<Boolean> {
    return JsonElementRequireReadonlyProperty(*keys) { jsonElement -> jsonElement.jsonPrimitive.boolean }
}

fun requireIntReadonlyProperty(vararg keys: String): JsonElementRequireReadonlyProperty<Int> {
    return JsonElementRequireReadonlyProperty(*keys) { jsonElement -> jsonElement.jsonPrimitive.int }
}

fun requireFloatReadonlyProperty(vararg keys: String): JsonElementRequireReadonlyProperty<Float> {
    return JsonElementRequireReadonlyProperty(*keys) { jsonElement -> jsonElement.jsonPrimitive.float }
}

fun requireDoubleReadonlyProperty(vararg keys: String): JsonElementRequireReadonlyProperty<Double> {
    return JsonElementRequireReadonlyProperty(*keys) { jsonElement -> jsonElement.jsonPrimitive.double }
}

fun <Type> requireListReadonlyProperty(vararg keys: String, mapElement: (JsonObject) -> Type): JsonElementRequireReadonlyProperty<List<Type>> {
    return JsonElementRequireReadonlyProperty(*keys) { jsonElement -> jsonElement.jsonArray.filterIsInstance<JsonObject>().map(mapElement) }
}
