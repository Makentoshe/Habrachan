package com.makentoshe.habrachan.di.common

import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.cache.CacheStorage
import com.makentoshe.habrachan.common.cache.InMemoryCacheStorage
import com.makentoshe.habrachan.common.entity.posts.Data
import com.makentoshe.habrachan.common.entity.posts.PostsResponse
import com.makentoshe.habrachan.common.network.request.GetPostsRequest
import com.makentoshe.habrachan.model.main.posts.PostsCache
import com.makentoshe.habrachan.model.main.posts.PostsResponseCache
import toothpick.config.Module
import toothpick.ktp.binding.bind

annotation class CacheScope

class CacheModule : Module() {

    private val postsCache = PostsCache(InMemoryCacheStorage())

    init {
        bind<Cache<Int, Data>>().toInstance(postsCache)
    }
}

