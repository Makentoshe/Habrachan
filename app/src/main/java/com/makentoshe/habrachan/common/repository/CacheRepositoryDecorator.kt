package com.makentoshe.habrachan.common.repository

import com.makentoshe.habrachan.common.cache.Cache

class CacheRepositoryDecorator<K, V>(
    private val cache: Cache<K, V>,
    private val repository: Repository<K, V>
): Repository<K, V> {
    override fun get(k: K): V? {
        val cachedResult = cache.get(k)
        if (cachedResult == null) {
            val result = repository.get(k)
            if (result != null) {
                cache.set(k, result)
            }
            return result
        } else {
            return cachedResult
        }
    }
}