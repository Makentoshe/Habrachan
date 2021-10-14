package com.makentoshe.habrachan.functional.com.makentoshe.habrachan

import com.makentoshe.habrachan.Option
import com.makentoshe.habrachan.Require
import com.makentoshe.habrachan.delegate.RequireReadonlyProperty
import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.delegate.OptionReadonlyProperty
import kotlin.reflect.KProperty

interface AnyWithFlatParameters {
    val parameters: Map<String, String>
}

/** Property for delegation a specified property that optional for [AnyWithFlatParameters] */
class AnyWithFlatParametersOptionReadonlyProperty<Owner: AnyWithFlatParameters, Type>(
    private val key: String,
    private val map: (String) -> Type,
) : OptionReadonlyProperty<Owner, Type>() {
    override fun getValue(thisRef: Owner, property: KProperty<*>): Option<Type> {
        return Option.from(thisRef.parameters[key]?.let(map))
    }
}

/** Property for delegation a specified property that requireable for [AnyWithFlatParameters], like password or login */
class AnyWithFlatParametersRequireReadonlyProperty<Owner: AnyWithFlatParameters, Type>(
    private val key: String,
    private val map: (String) -> Type
) : RequireReadonlyProperty<Owner, Type>() {
    override fun getValue(thisRef: Owner, property: KProperty<*>): Require<Type> {
        return Require(thisRef.parameters[key]?.let(map))
    }
}

fun <Owner: AnyWithFlatParameters, Type> requireReadonlyProperty(
    key: String,
    map: (String) -> Type,
): AnyWithFlatParametersRequireReadonlyProperty<Owner, Type> {
    return AnyWithFlatParametersRequireReadonlyProperty(key, map)
}
