package com.makentoshe.habrachan

import com.makentoshe.habrachan.delegate.OptionReadonlyProperty
import com.makentoshe.habrachan.delegate.Require2ReadonlyProperty
import com.makentoshe.habrachan.functional.Option2
import com.makentoshe.habrachan.functional.Require2
import kotlin.reflect.KProperty

interface AnyWithFlatParameters {
    val parameters: Map<String, String>
}

/** Property for delegation a specified property that optional for [AnyWithFlatParameters] */
class AnyWithFlatParametersOptionReadonlyProperty<Owner: AnyWithFlatParameters, Type>(
    private val key: String,
    private val map: (String) -> Type,
) : OptionReadonlyProperty<Owner, Type>() {
    override fun getValue(thisRef: Owner, property: KProperty<*>): Option2<Type> {
        return Option2.from(thisRef.parameters[key]?.let(map))
    }
}

/** Property for delegation a specified property that requireable for [AnyWithFlatParameters], like password or login */
class AnyWithFlatParametersRequire2ReadonlyProperty<Owner: AnyWithFlatParameters, Type>(
    private val key: String,
    private val map: (String) -> Type
) : Require2ReadonlyProperty<Owner, Type>() {
    override fun getValue(thisRef: Owner, property: KProperty<*>): Require2<Type> {
        return Require2(thisRef.parameters[key]?.let(map))
    }
}

fun <Owner: AnyWithFlatParameters, Type> requireReadonlyProperty(
    key: String,
    map: (String) -> Type,
): AnyWithFlatParametersRequire2ReadonlyProperty<Owner, Type> {
    return AnyWithFlatParametersRequire2ReadonlyProperty(key, map)
}
