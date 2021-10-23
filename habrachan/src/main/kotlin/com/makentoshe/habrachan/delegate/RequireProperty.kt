package com.makentoshe.habrachan.delegate

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.functional.Require2
import kotlinx.serialization.json.*
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class Require2ReadonlyProperty<Owner, Type> : ReadOnlyProperty<Owner, Require2<Type>>

abstract class Require2ReadWriteProperty<Owner, Type> : ReadWriteProperty<Owner, Require2<Type>>

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
    } catch (exception: NullPointerException) { // if declared keys more that presented
        val message = "IllegalArgumentException(message=${exception.message}, parameters=${thisRef.parameters})"
        throw IllegalArgumentException(message, exception.cause)
    } catch (exception: Exception) {
        throw Exception("Anus")
    }

}

fun <Type> requireReadonlyProperty(
    vararg keys: String,
    map: (JsonElement) -> Type
): JsonElementRequire2ReadonlyProperty<Type> {
    return JsonElementRequire2ReadonlyProperty(*keys, map = map)
}

fun requireStringReadonlyProperty(vararg keys: String): JsonElementRequire2ReadonlyProperty<String> {
    return JsonElementRequire2ReadonlyProperty(*keys) { jsonElement -> jsonElement.jsonPrimitive.content }
}

fun requireBooleanReadonlyProperty(vararg keys: String): JsonElementRequire2ReadonlyProperty<Boolean> {
    return JsonElementRequire2ReadonlyProperty(*keys) { jsonElement -> jsonElement.jsonPrimitive.boolean }
}

fun requireFloatReadonlyProperty(vararg keys: String): JsonElementRequire2ReadonlyProperty<Float> {
    return JsonElementRequire2ReadonlyProperty(*keys) { jsonElement -> jsonElement.jsonPrimitive.float }
}

fun requireDoubleReadonlyProperty(vararg keys: String): JsonElementRequire2ReadonlyProperty<Double> {
    return JsonElementRequire2ReadonlyProperty(*keys) { jsonElement -> jsonElement.jsonPrimitive.double }
}

fun <Type> requireListReadonlyProperty(
    vararg keys: String,
    mapElement: (JsonObject) -> Type
): JsonElementRequire2ReadonlyProperty<List<Type>> {
    return JsonElementRequire2ReadonlyProperty(*keys) { jsonElement ->
        jsonElement.jsonArray.filterIsInstance<JsonObject>().map(mapElement)
    }
}

fun <Type> requireReadonlyProperty(
    vararg mappers: PropertyMapper<Type>
) : Require2ReadonlyProperty<AnyWithVolumeParameters<JsonElement>, Type> {
    return JsonElementRequire2ReadonlyProperty2(*mappers)
}

interface PropertyMapper<Type> {
    val keys: Array<out String>
    val map: (JsonElement) -> Type?
}

fun <Type> propertyMapper(vararg keys: String, map: (JsonElement) -> Type?) = object : PropertyMapper<Type> {
    override val keys: Array<out String> = keys
    override val map: (JsonElement) -> Type? = map
}

fun intPropertyMapper(vararg keys: String) = object: PropertyMapper<Int> {
    override val keys: Array<out String> = keys
    override val map: (JsonElement) -> Int? = { jsonElement -> jsonElement.jsonPrimitive.int }
}

class JsonElementRequire2ReadonlyProperty2<Type>(
    private vararg val propertyMappers: PropertyMapper<Type>
) : Require2ReadonlyProperty<AnyWithVolumeParameters<JsonElement>, Type>() {

    override fun getValue(thisRef: AnyWithVolumeParameters<JsonElement>, property: KProperty<*>): Require2<Type> {
        return Require2(propertyMappers.mapNotNull { mapper -> getMapperValue(mapper, thisRef) }.firstOrNull())
    }

    private fun getMapperValue(mapper: PropertyMapper<Type>, thisRef: AnyWithVolumeParameters<JsonElement>): Type? {
        val initial = mapper.keys.firstOrNull()?.let(thisRef.parameters::get)
        val jsonElement = mapper.keys.drop(1).fold(initial) { jsonElement, key -> jsonElement?.jsonObject?.get(key) }
        return if (jsonElement == null) null else mapper.map(jsonElement)
    }
}
