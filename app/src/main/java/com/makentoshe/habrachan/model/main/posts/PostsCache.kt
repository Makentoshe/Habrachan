package com.makentoshe.habrachan.model.main.posts

import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.cache.CacheStorage
import com.makentoshe.habrachan.common.entity.posts.Data

class PostsCache(private val storageStrategy: CacheStorage<Int, Data>): Cache<Int, Data> {

    override fun get(k: Int) = storageStrategy.get(k)

    override fun set(k: Int, v: Data) = storageStrategy.set(k ,v)

}