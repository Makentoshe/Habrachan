package com.makentoshe.habrachan.common.cache

interface Cache<K, V> {
    fun get(k: K): V?
    fun set(k: K, v: V)
}