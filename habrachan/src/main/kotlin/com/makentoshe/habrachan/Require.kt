package com.makentoshe.habrachan

class Require<out V>(private val internalValue: V?) {

    val value: V
        get() = internalValue ?: throw IllegalStateException("Value is null")

    val nullableValue: V?
        get() = internalValue

    inline fun onValueNotNull(action: (V) -> Unit): Require<V> {
        nullableValue?.let(action)

        return this
    }
}

fun <T> Any?.toRequire() = Require<T>(this as T)
