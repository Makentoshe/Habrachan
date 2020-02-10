package com.makentoshe.habrachan.di.main.posts

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.ArticleDao
import com.makentoshe.habrachan.common.database.CommentDao
import com.makentoshe.habrachan.common.network.manager.HabrPostsManager
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.main.posts.ArticleRepository
import com.makentoshe.habrachan.view.main.posts.PostsFragment
import com.makentoshe.habrachan.viewmodel.main.posts.PostsViewModel
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class PostsFragmentModule(fragment: PostsFragment) : Module() {

    private val manager by inject<HabrPostsManager>()
    private val factory by inject<GetPostsRequestFactory>()
    private val articleDao by inject<ArticleDao>()
    private val commentDao by inject<CommentDao>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)

        val repository = ArticleRepository(factory, manager)
        val factory = PostsViewModel.Factory(repository, articleDao, commentDao)
        val viewmodel = ViewModelProviders.of(fragment, factory)[PostsViewModel::class.java]
        bind<PostsViewModel>().toInstance(viewmodel)
    }

    class Factory(private val fragment: PostsFragment) {
        fun build(position: Int): PostsFragmentModule {
            return PostsFragmentModule(fragment)
        }
    }
}