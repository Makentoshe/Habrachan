package com.makentoshe.habrachan.common.cache

interface CacheStorage<K, V> {
    fun get(k: K): V?
    fun set(k: K, v: V)
    fun clear()
}
