package com.makentoshe.habrachan.application.common.arena

import com.makentoshe.habrachan.functional.Either2

data class FlowArenaResponse<V, E>(val loading: Boolean, val content: Either2<V, E>) {

    inline fun <RLeft, RRight> bimap(fl: (V) -> RLeft, fr: (E) -> RRight): FlowArenaResponse<RLeft, RRight> {
        return FlowArenaResponse(loading, content.fold({ Either2.Left(fl(it)) }, { Either2.Right(fr(it)) }))
    }

    inline fun <R> mapLeft(f: (V) -> R): FlowArenaResponse<R, E> {
        return FlowArenaResponse(loading, content.fold({ Either2.Left(f(it)) }, { Either2.Right(it) }))
    }

    inline fun <R> mapRight(f: (E) -> R): FlowArenaResponse<V, R> {
        return FlowArenaResponse(loading, content.fold({ Either2.Left(it) }, { Either2.Right(f(it)) }))
    }
}
