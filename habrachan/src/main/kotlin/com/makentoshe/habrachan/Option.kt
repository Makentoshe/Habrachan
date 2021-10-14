package com.makentoshe.habrachan

sealed class Option<out V> {

    abstract val isEmpty: Boolean

    abstract val isNotEmpty: Boolean

    data class Value<V>(val value: V) : Option<V>() {
        override val isEmpty: Boolean = false
        override val isNotEmpty: Boolean = !isEmpty
    }

    object None : Option<Nothing>() {
        override val isEmpty: Boolean = true
        override val isNotEmpty: Boolean = !isEmpty
    }

    inline fun <R> fold(ifEmpty: () -> R, ifSome: (V) -> R): R = when (this) {
        is None -> ifEmpty()
        is Value<V> -> ifSome(value)
    }

    inline fun onNotEmpty(action: (V) -> Unit ): Option<V> {
        if (this is Value) action(value)
        return this
    }

    inline fun <B> map(f: (V) -> B): Option<B> = flatMap { a -> Value(f(a)) }

    inline fun <B> flatMap(f: (V) -> Option<B>): Option<B> = when (this) {
        is None -> this
        is Value -> f(value)
    }

    inline fun filter(predicate: (V) -> Boolean): Option<V> =
        flatMap { a -> if (predicate(a)) Value(a) else None }

    fun getOrNull(): V? = when (this) {
        is Value -> this.value
        else -> null
    }

    inline fun getOrThrow(exception: () -> Throwable = { IllegalStateException() }): V = when (this) {
        is Value -> this.value
        else -> throw exception()
    }

    companion object {
        fun <T> from(any: T?) = if (any == null) None else Value(any)
    }
}


fun <T> T?.toOption(): Option<T> = this?.let { Option.Value(it) } ?: Option.None
