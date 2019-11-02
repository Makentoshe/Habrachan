package com.makentoshe.habrachan.di.post

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.cache.Cache
import com.makentoshe.habrachan.common.database.PostsDao
import com.makentoshe.habrachan.common.entity.Data
import com.makentoshe.habrachan.common.repository.RawResourceRepository
import com.makentoshe.habrachan.di.ApplicationScope
import com.makentoshe.habrachan.viewmodel.post.PostFragmentViewModel
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import javax.inject.Provider

class PostFragmentViewModelProvider(
    private val fragment: Fragment,
    private val postId: Int
) : Provider<PostFragmentViewModel> {

    private val rawResourceRepository by inject<RawResourceRepository>()
    private val router by inject<Router>()
    private val postsDao by inject<PostsDao>()

    override fun get(): PostFragmentViewModel {
        val factory = createViewModelFactory()
        return ViewModelProviders.of(fragment, factory)[PostFragmentViewModel::class.java]
    }

    private fun createViewModelFactory(): ViewModelProvider.NewInstanceFactory {
        return PostFragmentViewModel.Factory(router, rawResourceRepository, postId, postsDao)
    }

    fun injects() = this.apply {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
    }
}