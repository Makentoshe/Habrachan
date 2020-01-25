package com.makentoshe.habrachan.di.main.posts

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.ArticleDao
import com.makentoshe.habrachan.common.network.manager.HabrPostsManager
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import com.makentoshe.habrachan.di.ApplicationScope
import com.makentoshe.habrachan.view.main.posts.PostsFragment
import com.makentoshe.habrachan.viewmodel.main.posts.DaoPostsRepository
import com.makentoshe.habrachan.viewmodel.main.posts.PostsViewModel
import com.makentoshe.habrachan.viewmodel.main.posts.PostsRepository
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class PostsFragmentModule(fragment: PostsFragment, position: Int) : Module() {

    private val manager by inject<HabrPostsManager>()
    private val factory by inject<GetPostsRequestFactory>()
    private val postsDao by inject<ArticleDao>()

    init {
        performInjections()
        performBindings(fragment, position)
    }

    private fun performInjections() {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
    }

    private fun performBindings(fragment: PostsFragment, position: Int) {
        val repository = PostsRepository(factory, manager)
        val daoPostsRepository = DaoPostsRepository(postsDao, repository)
        val factory = PostsViewModel.Factory(position, daoPostsRepository, postsDao)
        val viewmodel = ViewModelProviders.of(fragment, factory)[PostsViewModel::class.java]
        bind<PostsViewModel>().toInstance(viewmodel)
    }

    class Factory(private val fragment: PostsFragment) {
        fun build(position: Int): PostsFragmentModule {
            return PostsFragmentModule(fragment, position)
        }
    }
}