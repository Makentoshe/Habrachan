package com.makentoshe.habrachan.application.common.arena

import com.makentoshe.habrachan.functional.Result

/** Arena for performing any PUT, POST and similar actions that somehow a modifies server data */
abstract class CarryArena<in K, V> {

    /** Main method for performing network operation */
    abstract suspend fun internalSuspendCarry(key: K): Result<V>

    /** Method executes network and possible caches operations */
    abstract suspend fun suspendCarry(key: K): Result<V>
}