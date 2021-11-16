package com.makentoshe.habrachan.delegate.option

import com.makentoshe.habrachan.AnyWithFlatParameters
import com.makentoshe.habrachan.delegate.OptionReadonlyProperty
import com.makentoshe.habrachan.functional.Option2
import kotlin.reflect.KProperty

class StringOption2ReadonlyProperty<Type>(
    private vararg val keys: String,
    private val map: (String) -> Type
) : OptionReadonlyProperty<AnyWithFlatParameters, Type>() {

    override fun getValue(thisRef: AnyWithFlatParameters, property: KProperty<*>): Option2<Type> = try {
        Option2.from(thisRef.parameters[keys.joinToString(".")]?.let { map(it) })
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
