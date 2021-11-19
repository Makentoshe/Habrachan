package com.makentoshe.habrachan.functional

sealed class Either2<out TLeft, out TRight> {

    internal abstract val isLeft: Boolean

    internal abstract val isRight: Boolean

    data class Left<T>(val value: T) : Either2<T, Nothing>() {
        override val isLeft = true
        override val isRight = false

        override fun toString(): String = "Either.Left($value)"
    }

    data class Right<T>(val value: T) : Either2<Nothing, T>() {
        override val isLeft = false
        override val isRight = true

        override fun toString(): String = "Either.Right($value)"
    }

    inline fun <R> fold(ifLeft: (TLeft) -> R, ifRight: (TRight) -> R): R = when (this) {
        is Right -> ifRight(value)
        is Left -> ifLeft(value)
    }

    inline fun <R> mapLeft(f: (TLeft) -> R): Either2<R, TRight> {
        return fold({ Left(f(it)) }, { Right(it) })
    }

    inline fun <R> mapRight(f: (TRight) -> R): Either2<TLeft, R> {
        return fold({ Left(it) }, { Right(f(it)) })
    }

    inline fun <RLeft, RRight> bimap(fl: (TLeft) -> RLeft, fr: (TRight) -> RRight): Either2<RLeft, RRight> {
        return fold({ Left(fl(it)) }, { Right(fr(it)) })
    }

    inline fun onRight(f: (TRight) -> Unit) : Either2<TLeft, TRight> {
        if (this is Right) f(this.value)
        return this
    }

    inline fun onLeft(f: (TLeft) -> Unit) : Either2<TLeft, TRight> {
        if (this is Left) f(this.value)
        return this
    }

    fun isLeft(): Boolean = isLeft

    fun isRight(): Boolean = isRight

}

fun <TLeft> Either2<TLeft, Any>.left(): TLeft {
    return (this as Either2.Left).value
}

fun <TLeft> Either2<TLeft, Any>.leftOrNull(): TLeft? {
    return (this as? Either2.Left)?.value
}

fun <TRight> Either2<Unit, TRight>.right(): TRight {
    return (this as Either2.Right).value
}

fun <TRight> Either2<Any, TRight>.rightOrNull(): TRight? {
    return (this as? Either2.Right)?.value
}
