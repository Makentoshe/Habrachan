package com.makentoshe.habrachan.di.posts

import com.makentoshe.habrachan.common.entity.posts.PostsResponse
import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.cache.InMemoryCacheStorage
import com.makentoshe.habrachan.common.network.request.GetPostsRequest
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import com.makentoshe.habrachan.common.network.manager.HabrPostsManager
import com.makentoshe.habrachan.model.posts.PostsBroadcastReceiver
import com.makentoshe.habrachan.model.posts.PostsResponseCache
import com.makentoshe.habrachan.ui.posts.PostsFragmentUi
import okhttp3.OkHttpClient
import toothpick.config.Module
import toothpick.ktp.binding.bind

class PostsFragmentModule : Module() {
    init {
        bind<PostsFragmentUi>().toInstance(PostsFragmentUi())

        val client = OkHttpClient.Builder().build()
        val manager = HabrPostsManager.Builder(client).build()
        bind<HabrPostsManager>().toInstance(manager)

        val broadcastReceiver = PostsBroadcastReceiver()
        bind<PostsBroadcastReceiver>().toInstance(broadcastReceiver)

        val cache = PostsResponseCache(InMemoryCacheStorage())
        bind<Cache<GetPostsRequest, PostsResponse>>().toInstance(cache)

        val factory = GetPostsRequestFactory(
            client = "85cab69095196f3.89453480", api = "173984950848a2d27c0cc1c76ccf3d6d3dc8255b", token = null
        )
        bind<GetPostsRequestFactory>().toInstance(factory)
    }
}
