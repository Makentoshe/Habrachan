package com.makentoshe.habrachan.functional

class Require2<out V>(private val internalValue: V?) {

    val value: V
        get() = internalValue ?: throw IllegalStateException("Value is null")

    val nullableValue: V?
        get() = internalValue

    inline fun onValueNotNull(action: (V) -> Unit): Require2<V> {
        nullableValue?.let(action)

        return this
    }
}

fun <T> T?.toRequire2() = Require2<T>(this)
