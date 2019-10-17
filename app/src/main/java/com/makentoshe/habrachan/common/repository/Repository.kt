package com.makentoshe.habrachan.common.repository

interface Repository<K, V> {
    fun get(k: K): V?
}