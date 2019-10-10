package com.makentoshe.habrachan.di.posts

import com.makentoshe.habrachan.common.model.cache.Cache
import com.makentoshe.habrachan.common.model.cache.PostsResponseDatabaseCacheStorage
import com.makentoshe.habrachan.common.model.database.Database
import com.makentoshe.habrachan.model.posts.PostsResponseCache
import com.makentoshe.habrachan.common.model.network.postsalt.GetRawRequest
import com.makentoshe.habrachan.common.model.network.postsalt.HabrPostsManager
import com.makentoshe.habrachan.common.model.network.postsalt.entity.PostsResponse
import com.makentoshe.habrachan.model.posts.PostsBroadcastReceiver
import com.makentoshe.habrachan.ui.posts.PostsFragmentUi
import com.makentoshe.habrachan.common.model.cache.PostsRequestCache
import okhttp3.OkHttpClient
import toothpick.config.Module
import toothpick.ktp.binding.bind

class PostsFragmentModule : Module() {
    init {
        bind<PostsFragmentUi>().toInstance(PostsFragmentUi())

        val client = OkHttpClient.Builder().build()
        bind<HabrPostsManager>().toInstance(HabrPostsManager.build(client = client))

        val broadcastReceiver = PostsBroadcastReceiver()
        bind<PostsBroadcastReceiver>().toInstance(broadcastReceiver)

        val databasePosts = Database.Builder().posts("posts").build()
        val cache = PostsResponseCache(PostsResponseDatabaseCacheStorage(databasePosts))
        bind<Cache<GetRawRequest, PostsResponse>>().toInstance(cache)

        val databaseConfig = Database.Builder().posts("posts").configuration().build()
        val configCache = PostsRequestCache(databaseConfig)
        bind<Cache<Int, GetRawRequest>>().toInstance(configCache)
    }
}
