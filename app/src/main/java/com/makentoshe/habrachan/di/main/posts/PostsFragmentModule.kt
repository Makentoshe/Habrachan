package com.makentoshe.habrachan.di.main.posts

import android.content.Context
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.cache.InMemoryCacheStorage
import com.makentoshe.habrachan.common.database.RequestStorage
import com.makentoshe.habrachan.common.database.SharedRequestStorage
import com.makentoshe.habrachan.common.entity.posts.PostsResponse
import com.makentoshe.habrachan.common.network.manager.HabrPostsManager
import com.makentoshe.habrachan.common.network.request.GetPostsRequest
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import com.makentoshe.habrachan.model.main.posts.PostsBroadcastReceiver
import com.makentoshe.habrachan.model.main.posts.PostsResponseCache
import com.makentoshe.habrachan.ui.main.posts.PostsFragmentUi
import com.makentoshe.habrachan.viewmodel.main.posts.PostsPageViewModelFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import toothpick.config.Module
import toothpick.ktp.binding.bind

class PostsFragmentModule(context: Context) : Module() {

    init {
        bind<PostsFragmentUi>().toInstance(PostsFragmentUi())

        val broadcastReceiver = PostsBroadcastReceiver()
        bind<PostsBroadcastReceiver>().toInstance(broadcastReceiver)

        val requestStorage = SharedRequestStorage.Builder().build(context)
        val factory = GetPostsRequestFactory(
            "85cab69095196f3.89453480", "173984950848a2d27c0cc1c76ccf3d6d3dc8255b", null, requestStorage
        )
        bind<GetPostsRequestFactory>().toInstance(factory)

        val cache = PostsResponseCache(InMemoryCacheStorage())
        bind<Cache<GetPostsRequest, PostsResponse>>().toInstance(cache)

        val manager = HabrPostsManager.Builder(createClient()).build()
        bind<GetPostsRequestFactory>().toInstance(factory)

        val postsPageViewModelFactory =
            PostsPageViewModelFactory(manager, factory, cache)
        bind<PostsPageViewModelFactory>().toInstance(postsPageViewModelFactory)

        bind<RequestStorage>().toInstance(requestStorage)
    }

    private fun createClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            if (!BuildConfig.DEBUG) return@apply
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.HEADERS
            addInterceptor(logging)
        }.build()
    }
}
