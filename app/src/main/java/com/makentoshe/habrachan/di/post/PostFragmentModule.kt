package com.makentoshe.habrachan.di.post

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.cache.ImagesCache
import com.makentoshe.habrachan.common.cache.InMemoryCacheStorage
import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.common.network.manager.HabrPostsManager
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import com.makentoshe.habrachan.di.ApplicationScope
import com.makentoshe.habrachan.di.common.CacheScope
import com.makentoshe.habrachan.di.common.NetworkScope
import com.makentoshe.habrachan.model.post.Converter
import com.makentoshe.habrachan.model.post.OkHttpPublicationParser
import com.makentoshe.habrachan.ui.post.PostFragmentUi
import com.makentoshe.habrachan.view.post.PostFragment
import com.makentoshe.habrachan.viewmodel.post.PostFragmentViewModel
import okhttp3.OkHttpClient
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

annotation class PostFragmentScope

class PostFragmentModule private constructor() : Module() {

    private val postsCache by inject<Cache<Int, Data>>()

    private val requestFactory by inject<GetPostsRequestFactory>()

    private val postsManager by inject<HabrPostsManager>()

    private val client by inject<OkHttpClient>()

    private val converter by inject<Converter>()

    init {
        val postFragmentUi = PostFragmentUi()
        bind<PostFragmentUi>().toInstance(postFragmentUi)
    }

    private fun bindViewModel(fragment: PostFragment, position: Int, page: Int) {
        val imagesCache = ImagesCache(InMemoryCacheStorage())
        val publicationParser = OkHttpPublicationParser(client, imagesCache, converter)
        val factory = PostFragmentViewModel.Factory(
            position, page, postsCache, requestFactory, postsManager, publicationParser
        )
        val viewModel = ViewModelProviders.of(fragment, factory)[PostFragmentViewModel::class.java]

        bind<PostFragmentViewModel>().toInstance(viewModel)
    }

    class Builder(private val position: Int, private val page: Int) {

        fun build(fragment: PostFragment): PostFragmentModule {
            val module = PostFragmentModule()
            val scope = Toothpick.openScope(ApplicationScope::class.java)
            scope.inject(module)
            module.bindViewModel(fragment, position, page)
            return module
        }
    }
}