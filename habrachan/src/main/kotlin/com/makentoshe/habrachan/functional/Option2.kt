package com.makentoshe.habrachan.functional

sealed class Option2<out V> {

    abstract val isEmpty: Boolean

    abstract val isNotEmpty: Boolean

    abstract val nullableValue: V?

    data class Value<V>(val value: V) : Option2<V>() {
        override val isEmpty: Boolean = false
        override val isNotEmpty: Boolean = !isEmpty

        override val nullableValue: V? = value

        override fun toString(): String {
            return "Option(value=$value)"
        }
    }

    object None : Option2<Nothing>() {
        override val isEmpty: Boolean = true
        override val isNotEmpty: Boolean = !isEmpty

        override val nullableValue: Nothing? = null

        override fun toString(): String {
            return "Option(None)"
        }
    }

    inline fun <R> fold(ifEmpty: () -> R, ifSome: (V) -> R): R = when (this) {
        is None -> ifEmpty()
        is Value<V> -> ifSome(value)
    }

    inline fun onNotEmpty(action: (V) -> Unit): Option2<V> {
        if (this is Value) action(value)
        return this
    }

    inline fun <B> map(f: (V) -> B): Option2<B> = flatMap { a -> Value(f(a)) }

    inline fun <B> flatMap(f: (V) -> Option2<B>): Option2<B> = when (this) {
        is None -> this
        is Value -> f(value)
    }

    inline fun filter(predicate: (V) -> Boolean): Option2<V> =
        flatMap { a -> if (predicate(a)) Value(a) else None }

    fun getOrNull(): V? = when (this) {
        is Value -> this.value
        else -> null
    }

    inline fun getOrThrow(exception: () -> Throwable = { IllegalStateException() }): V = when (this) {
        is Value -> this.value
        else -> throw exception()
    }

    @Suppress("USELESS_CAST") //  without casting toString will be recursive
    override fun toString() : String = when(this) {
        is Value -> (this as Value).toString()
        else -> (this as None).toString()
    }

    companion object {
        fun <T> from(any: T?) = if (any == null) None else Value(any)
    }
}


fun <T> T?.toOption(): Option2<T> = this?.let { Option2.Value(it) } ?: Option2.None

fun <T> T?.toOption2(): Option2<T> = this?.let { Option2.Value(it) } ?: Option2.None
