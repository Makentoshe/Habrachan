package com.makentoshe.habrachan.di.post

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.cache.ImagesCache
import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.common.network.manager.HabrPostsManager
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import com.makentoshe.habrachan.common.repository.InputStreamRepository
import com.makentoshe.habrachan.common.repository.Repository
import com.makentoshe.habrachan.di.ApplicationScope
import com.makentoshe.habrachan.model.post.PublicationRepository
import com.makentoshe.habrachan.ui.post.PostFragmentUi
import com.makentoshe.habrachan.view.post.PostFragment
import com.makentoshe.habrachan.viewmodel.post.PostFragmentViewModel
import okhttp3.OkHttpClient
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject
import toothpick.smoothie.lifecycle.closeOnDestroy
import java.io.InputStream

annotation class PostFragmentScope

class PostFragmentModule private constructor() : Module() {

    private val postsCache by inject<Cache<Int, Data>>()

    private val requestFactory by inject<GetPostsRequestFactory>()

    private val postsManager by inject<HabrPostsManager>()

    private val client by inject<OkHttpClient>()

    private val imagesCache by inject<ImagesCache>()

    init {
        val postFragmentUi = PostFragmentUi()
        bind<PostFragmentUi>().toInstance(postFragmentUi)
    }

    private fun bindViewModel(fragment: PostFragment, position: Int, page: Int) {
        val publicationRepository = PublicationRepository(postsCache, requestFactory, postsManager)
        val factory = PostFragmentViewModel.Factory(position, page, publicationRepository)
        val viewModel = ViewModelProviders.of(fragment, factory)[PostFragmentViewModel::class.java]

        bind<PostFragmentViewModel>().toInstance(viewModel)
    }

    private fun bind() {
        bind<InputStreamRepository>().toInstance(InputStreamRepository(client))
    }

    class Builder(private val position: Int, private val page: Int) {

        fun build(fragment: PostFragment): PostFragmentModule {
            val module = PostFragmentModule()
            Toothpick.openScope(ApplicationScope::class.java).closeOnDestroy(fragment).inject(module)
            module.bindViewModel(fragment, position, page)
            module.bind()
            return module
        }
    }
}
