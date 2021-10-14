package com.makentoshe.habrachan.functional.com.makentoshe.habrachan

import com.makentoshe.habrachan.Option
import com.makentoshe.habrachan.Require
import com.makentoshe.habrachan.delegate.RequireReadonlyProperty
import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.delegate.OptionReadonlyProperty
import kotlin.reflect.KProperty

interface AnyWithVolumeParameters<Type> {
    val parameters: Map<String, Type>
}

/** Property for delegation a specified property that optional for [AnyWithVolumeParameters] */
class AnyWithVolumeParametersOptionReadonlyProperty<Owner: AnyWithVolumeParameters<Type>, Type>(
    private val key: String,
) : OptionReadonlyProperty<Owner, Type>() {
    override fun getValue(thisRef: Owner, property: KProperty<*>): Option<Type> {
        return Option.from(thisRef.parameters[key])
    }
}

/** Property for delegation a specified property that requireable for [AnyWithVolumeParameters], like password or login */
class AnyWithVolumeParametersRequireReadonlyProperty<Owner: AnyWithVolumeParameters<Type>, Type>(
    private val key: String,
) : RequireReadonlyProperty<Owner, Type>() {
    override fun getValue(thisRef: Owner, property: KProperty<*>): Require<Type> {
        return Require(thisRef.parameters[key])
    }
}

fun <Owner: AnyWithVolumeParameters<Type>, Type> requireReadonlyProperty(
    key: String,
): AnyWithVolumeParametersRequireReadonlyProperty<Owner, Type> {
    return AnyWithVolumeParametersRequireReadonlyProperty(key)
}
