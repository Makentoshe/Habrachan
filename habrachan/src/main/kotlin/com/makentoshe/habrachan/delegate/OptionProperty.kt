package com.makentoshe.habrachan.functional.com.makentoshe.habrachan.delegate

import com.makentoshe.habrachan.Option
import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.AnyWithVolumeParameters
import kotlinx.serialization.json.*
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class OptionReadWriteProperty<Owner, Type>: ReadWriteProperty<Owner, Option<Type>>

abstract class OptionReadonlyProperty<Owner, Type>: ReadOnlyProperty<Owner, Option<Type>>

/** Property for delegation a specified property that requireable for [Article], like id or author */
class JsonElementOptionReadonlyProperty<Type>(
    private vararg val keys: String,
    private val map: (JsonElement) -> Type
) : OptionReadonlyProperty<AnyWithVolumeParameters<JsonElement>, Type>() {

    override fun getValue(thisRef: AnyWithVolumeParameters<JsonElement>, property: KProperty<*>): Option<Type> {
        val initial = thisRef.parameters[keys.first()]
        if (initial is JsonNull) return Option.None
        return Option.from(keys.drop(1).fold(initial) { jsonElement, key -> jsonElement!!.jsonObject[key] }?.let(map))
    }
}

fun <Type> optionReadonlyProperty(vararg keys: String, map: (JsonElement) -> Type): JsonElementOptionReadonlyProperty<Type> {
    return JsonElementOptionReadonlyProperty(*keys, map = map)
}

fun optionStringReadonlyProperty(vararg keys: String): JsonElementOptionReadonlyProperty<String> {
    return JsonElementOptionReadonlyProperty(*keys) { jsonElement -> jsonElement.jsonPrimitive.content }
}

fun optionIntReadonlyProperty(vararg keys: String): JsonElementOptionReadonlyProperty<Int> {
    return JsonElementOptionReadonlyProperty(*keys) { jsonElement -> jsonElement.jsonPrimitive.int }
}

fun <Type> optionListReadonlyProperty(vararg keys: String, mapElement: (JsonElement) -> Type): JsonElementOptionReadonlyProperty<List<Type>> {
    return JsonElementOptionReadonlyProperty(*keys) { jsonElement ->
        jsonElement.jsonArray.map(mapElement)
    }
}
