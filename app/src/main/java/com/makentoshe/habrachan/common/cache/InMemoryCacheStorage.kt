package com.makentoshe.habrachan.common.cache

import java.util.concurrent.ConcurrentHashMap

class InMemoryCacheStorage<K, V> : CacheStorage<K, V> {

    private val map = ConcurrentHashMap<K, V>()

    override fun get(k: K): V? {
        return map[k]
    }

    override fun set(k: K, v: V) {
        map[k] = v
    }

    override fun clear() {
        map.clear()
    }
}
