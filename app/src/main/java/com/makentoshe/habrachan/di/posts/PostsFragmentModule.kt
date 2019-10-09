package com.makentoshe.habrachan.di.posts

import com.makentoshe.habrachan.common.model.cache.Cache
import com.makentoshe.habrachan.common.model.cache.InMemoryCacheStorage
import com.makentoshe.habrachan.common.model.cache.PostsResponseDatabaseCacheStorage
import com.makentoshe.habrachan.common.model.database.Database
import com.makentoshe.habrachan.model.posts.PostsResponseCache
import com.makentoshe.habrachan.common.model.network.postsalt.GetRawRequest
import com.makentoshe.habrachan.common.model.network.postsalt.HabrPostsManager
import com.makentoshe.habrachan.common.model.network.postsalt.entity.PostsResponse
import com.makentoshe.habrachan.ui.posts.PostsFragmentUi
import okhttp3.OkHttpClient
import toothpick.config.Module
import toothpick.ktp.binding.bind

class PostsFragmentModule : Module() {
    init {
        bind<PostsFragmentUi>().toInstance(PostsFragmentUi())

        val client = OkHttpClient.Builder().build()
        bind<HabrPostsManager>().toInstance(HabrPostsManager.build(client = client))

        val database = Database.Builder().posts("posts").build()
        val cache = PostsResponseCache(PostsResponseDatabaseCacheStorage(database))
        bind<Cache<GetRawRequest, PostsResponse>>().toInstance(cache)
    }
}