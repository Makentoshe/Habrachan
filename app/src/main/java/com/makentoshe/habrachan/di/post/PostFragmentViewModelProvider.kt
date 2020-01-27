package com.makentoshe.habrachan.di.post

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.ArticleDao
import com.makentoshe.habrachan.common.network.manager.HabrPostManager
import com.makentoshe.habrachan.common.network.request.GetPostRequestFactory
import com.makentoshe.habrachan.common.repository.RawResourceRepository
import com.makentoshe.habrachan.model.post.DaoPostRepository
import com.makentoshe.habrachan.model.post.PostRepository
import com.makentoshe.habrachan.viewmodel.post.PostFragmentViewModel
import toothpick.ktp.delegate.inject
import javax.inject.Provider

/* Provides PostFragmentViewModel for PostFragment*/
class PostFragmentViewModelProvider(
    private val fragment: Fragment,
    private val postId: Int
) : Provider<PostFragmentViewModel> {

    private val rawResourceRepository by inject<RawResourceRepository>()
    private val postsDao by inject<ArticleDao>()
    private val requestFactory by inject<GetPostRequestFactory>()
    private val manager by inject<HabrPostManager>()

    override fun get(): PostFragmentViewModel {
        val factory = createViewModelFactory()
        return ViewModelProviders.of(fragment, factory)[PostFragmentViewModel::class.java]
    }

    private fun createViewModelFactory(): ViewModelProvider.NewInstanceFactory {
        val postRepository = PostRepository(requestFactory, manager)
        val daoPostRepository = DaoPostRepository(postsDao, postRepository)
        return PostFragmentViewModel.Factory(rawResourceRepository, daoPostRepository, postId)
    }
}
