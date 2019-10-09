package com.makentoshe.habrachan.common.model.cache

class InMemoryCacheStorage<K, V> : CacheStorage<K, V> {

    private val map = HashMap<K, V>()

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
