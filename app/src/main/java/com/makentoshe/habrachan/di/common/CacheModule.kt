package com.makentoshe.habrachan.di.common

import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.cache.ImagesCache
import com.makentoshe.habrachan.common.cache.InMemoryCacheStorage
import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.model.main.posts.PostsCache
import toothpick.config.Module
import toothpick.ktp.binding.bind

annotation class CacheScope

class CacheModule : Module() {

    private val postsCache = PostsCache(InMemoryCacheStorage())
    private val imagesCache = ImagesCache(InMemoryCacheStorage())

    init {
        bind<Cache<Int, Data>>().toInstance(postsCache)
        bind<ImagesCache>().toInstance(imagesCache)
    }
}

