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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Require2<*>

        if (internalValue != other.internalValue) return false

        return true
    }

    override fun hashCode(): Int {
        return internalValue?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Require($nullableValue)"
    }

}

fun <T> T?.toRequire2() = Require2<T>(this)
