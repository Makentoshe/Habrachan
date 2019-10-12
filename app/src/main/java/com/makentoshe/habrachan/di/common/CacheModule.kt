package com.makentoshe.habrachan.di.common

import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.cache.CacheStorage
import com.makentoshe.habrachan.common.cache.InMemoryCacheStorage
import com.makentoshe.habrachan.common.entity.posts.PostsResponse
import com.makentoshe.habrachan.common.network.request.GetPostsRequest
import com.makentoshe.habrachan.model.main.posts.PostsResponseCache
import toothpick.config.Module
import toothpick.ktp.binding.bind

annotation class CacheScope

class CacheModule: Module() {
    init {
        val cacheStorage = InMemoryCacheStorage<GetPostsRequest, PostsResponse>()
        bind<CacheStorage<GetPostsRequest, PostsResponse>>().toInstance(cacheStorage)

        val cache = PostsResponseCache(InMemoryCacheStorage())
        bind<Cache<GetPostsRequest, PostsResponse>>().toInstance(cache)
    }
}

