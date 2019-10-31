package com.makentoshe.habrachan.di.post

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.common.network.manager.HabrPostsManager
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import com.makentoshe.habrachan.common.repository.RawResourceRepository
import com.makentoshe.habrachan.common.repository.RxRepositoryDecorator
import com.makentoshe.habrachan.di.ApplicationScope
import com.makentoshe.habrachan.common.repository.CacheRepositoryDecorator
import com.makentoshe.habrachan.model.post.PublicationRepository
import com.makentoshe.habrachan.viewmodel.post.PostFragmentViewModel
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import javax.inject.Provider

class PostFragmentViewModelProvider(
    private val fragment: Fragment,
    private val position: Int,
    private val page: Int
) : Provider<PostFragmentViewModel> {

    private val postsCache by inject<Cache<Int, Data>>()
    private val requestFactory by inject<GetPostsRequestFactory>()
    private val postsManager by inject<HabrPostsManager>()
    private val rawResourceRepository by inject<RawResourceRepository>()
    private val router by inject<Router>()

    override fun get(): PostFragmentViewModel {
        val factory = createViewModelFactory()
        return ViewModelProviders.of(fragment, factory)[PostFragmentViewModel::class.java]
    }

    private fun createViewModelFactory(): ViewModelProvider.NewInstanceFactory {
        val publicationRepository = PublicationRepository(postsCache, requestFactory, postsManager)
        return PostFragmentViewModel.Factory(position, page, publicationRepository, router, rawResourceRepository)
    }

    fun injects() = this.apply {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
    }
}