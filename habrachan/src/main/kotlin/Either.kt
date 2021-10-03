package com.makentoshe.habrachan.functional

sealed class Either<out TLeft, out TRight> {

    internal abstract val isLeft: Boolean

    internal abstract val isRight: Boolean

    data class Left<T>(val value: T) : Either<T, Nothing>() {
        override val isLeft = true
        override val isRight = false

        override fun toString(): String = "Either.Left($value)"
    }

    data class Right<T>(val value: T) : Either<Nothing, T>() {
        override val isLeft = false
        override val isRight = true

        override fun toString(): String = "Either.Right($value)"
    }

    inline fun <R> fold(ifLeft: (TLeft) -> R, ifRight: (TRight) -> R): R = when (this) {
        is Right -> ifRight(value)
        is Left -> ifLeft(value)
    }

    inline fun <R> mapLeft(f: (TLeft) -> R): Either<R, TRight> {
        return fold({ Left(f(it)) }, { Right(it) })
    }

    inline fun <R> mapRight(f: (TRight) -> R): Either<TLeft, R> {
        return fold({ Left(it) }, { Right(f(it)) })
    }

    inline fun <RLeft, RRight> bimap(fl: (TLeft) -> RLeft, fr: (TRight) -> RRight): Either<RLeft, RRight> {
        return fold({ Left(fl(it)) }, { Right(fr(it)) })
    }

    fun isLeft(): Boolean = isLeft

    fun isRight(): Boolean = isRight

}
