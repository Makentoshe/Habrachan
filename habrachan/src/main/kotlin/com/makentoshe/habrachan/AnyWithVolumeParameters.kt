package com.makentoshe.habrachan

import com.makentoshe.habrachan.delegate.OptionReadonlyProperty
import com.makentoshe.habrachan.delegate.Require2ReadonlyProperty
import com.makentoshe.habrachan.functional.Option2
import com.makentoshe.habrachan.functional.Require2
import kotlin.reflect.KProperty

interface AnyWithVolumeParameters<Type> {
    val parameters: Map<String, Type>
}

/** Property for delegation a specified property that optional for [AnyWithVolumeParameters] */
class AnyWithVolumeParametersOptionReadonlyProperty<Owner: AnyWithVolumeParameters<Type>, Type>(
    private val key: String,
) : OptionReadonlyProperty<Owner, Type>() {
    override fun getValue(thisRef: Owner, property: KProperty<*>): Option2<Type> {
        return Option2.from(thisRef.parameters[key])
    }
}

/** Property for delegation a specified property that requireable for [AnyWithVolumeParameters], like password or login */
class AnyWithVolumeParametersRequire2ReadonlyProperty<Owner: AnyWithVolumeParameters<Type>, Type>(
    private val key: String,
) : Require2ReadonlyProperty<Owner, Type>() {
    override fun getValue(thisRef: Owner, property: KProperty<*>): Require2<Type> {
        return Require2(thisRef.parameters[key])
    }
}

fun <Owner: AnyWithVolumeParameters<Type>, Type> requireReadonlyProperty(
    key: String,
): AnyWithVolumeParametersRequire2ReadonlyProperty<Owner, Type> {
    return AnyWithVolumeParametersRequire2ReadonlyProperty(key)
}
