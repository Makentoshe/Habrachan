package com.makentoshe.habrachan.delegate

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.functional.Require2
import kotlinx.serialization.json.*
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class Require2ReadonlyProperty<Owner, Type>: ReadOnlyProperty<Owner, Require2<Type>>

abstract class Require2ReadWriteProperty<Owner, Type>: ReadWriteProperty<Owner, Require2<Type>>

class JsonElementRequire2ReadonlyProperty<Type>(
    private vararg val keys: String,
    private val map: (JsonElement) -> Type
) : Require2ReadonlyProperty<AnyWithVolumeParameters<JsonElement>, Type>() {

    override fun getValue(thisRef: AnyWithVolumeParameters<JsonElement>, property: KProperty<*>): Require2<Type> = try {
        val initial = thisRef.parameters[keys.first()]
        Require2(keys.drop(1).fold(initial) { jsonElement, key -> jsonElement!!.jsonObject[key] }?.let(map))
    } catch (exception: IllegalArgumentException) {
        val message = "IllegalArgumentException(message=${exception.message}, parameters=${thisRef.parameters})"
        throw IllegalArgumentException(message, exception.cause)
    }

}

fun <Type> requireReadonlyProperty(vararg keys: String, map: (JsonElement) -> Type): JsonElementRequire2ReadonlyProperty<Type> {
    return JsonElementRequire2ReadonlyProperty(*keys, map = map)
}

fun requireStringReadonlyProperty(vararg keys: String): JsonElementRequire2ReadonlyProperty<String> {
    return JsonElementRequire2ReadonlyProperty(*keys) { jsonElement -> jsonElement.jsonPrimitive.content }
}

fun requireBooleanReadonlyProperty(vararg keys: String): JsonElementRequire2ReadonlyProperty<Boolean> {
    return JsonElementRequire2ReadonlyProperty(*keys) { jsonElement -> jsonElement.jsonPrimitive.boolean }
}

fun requireIntReadonlyProperty(vararg keys: String): JsonElementRequire2ReadonlyProperty<Int> {
    return JsonElementRequire2ReadonlyProperty(*keys) { jsonElement -> jsonElement.jsonPrimitive.int }
}

fun requireFloatReadonlyProperty(vararg keys: String): JsonElementRequire2ReadonlyProperty<Float> {
    return JsonElementRequire2ReadonlyProperty(*keys) { jsonElement -> jsonElement.jsonPrimitive.float }
}

fun requireDoubleReadonlyProperty(vararg keys: String): JsonElementRequire2ReadonlyProperty<Double> {
    return JsonElementRequire2ReadonlyProperty(*keys) { jsonElement -> jsonElement.jsonPrimitive.double }
}

fun <Type> requireListReadonlyProperty(vararg keys: String, mapElement: (JsonObject) -> Type): JsonElementRequire2ReadonlyProperty<List<Type>> {
    return JsonElementRequire2ReadonlyProperty(*keys) { jsonElement -> jsonElement.jsonArray.filterIsInstance<JsonObject>().map(mapElement) }
}
