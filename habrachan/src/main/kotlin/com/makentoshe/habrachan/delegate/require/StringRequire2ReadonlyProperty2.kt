package com.makentoshe.habrachan.delegate.require

import com.makentoshe.habrachan.AnyWithFlatParameters
import com.makentoshe.habrachan.delegate.Require2ReadonlyProperty
import com.makentoshe.habrachan.functional.Require2
import kotlin.reflect.KProperty

class StringRequire2ReadonlyProperty<Type>(
    private vararg val keys: String,
    private val map: (String) -> Type
) : Require2ReadonlyProperty<AnyWithFlatParameters, Type>() {

    override fun getValue(thisRef: AnyWithFlatParameters, property: KProperty<*>): Require2<Type> = try {
        Require2(thisRef.parameters[keys.joinToString(".")]?.let { map(it) })
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
