package com.makentoshe.habrachan.common.repository

import io.reactivex.Single

class RxRepositoryDecorator<K, V>(private val repository: Repository<K, V>) : Repository<K, Single<V>> {
    override fun get(k: K): Single<V> {
        val result = repository.get(k)
        return if (result != null) {
            Single.just(result)
        } else {
            Single.just(NullPointerException("RxRpository: value by key $k is null")).map { throw it }
        }
    }
}