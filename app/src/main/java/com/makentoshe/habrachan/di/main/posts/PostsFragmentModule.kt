package com.makentoshe.habrachan.di.main.posts

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.common.database.ArticleDao
import com.makentoshe.habrachan.common.database.AvatarDao
import com.makentoshe.habrachan.common.database.CommentDao
import com.makentoshe.habrachan.common.database.SessionDao
import com.makentoshe.habrachan.common.network.manager.HabrArticleManager
import com.makentoshe.habrachan.common.network.request.GetArticlesRequest
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.main.posts.ArticleRepository
import com.makentoshe.habrachan.view.main.posts.PostsFragment
import com.makentoshe.habrachan.viewmodel.main.posts.ArticlesViewModel
import com.makentoshe.habrachan.viewmodel.main.posts.PostsViewModel
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject
import javax.inject.Provider

class PostsFragmentModule(fragment: PostsFragment) : Module() {

    private val manager by inject<HabrArticleManager>()
    private val factory by inject<GetArticlesRequest.Builder>()
    private val articleDao by inject<ArticleDao>()
    private val commentDao by inject<CommentDao>()
    private val avatarDao by inject<AvatarDao>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
        val repository = ArticleRepository(factory, manager)
        val factory = PostsViewModel.Factory(repository, articleDao, commentDao, avatarDao)
        val viewmodel = ViewModelProviders.of(fragment, factory)[PostsViewModel::class.java]
        bind<PostsViewModel>().toInstance(viewmodel)

        bind<ArticlesViewModel>().toProviderInstance(ArticlesViewModelProvider(fragment))
    }

    class Factory(private val fragment: PostsFragment) {
        fun build(position: Int): PostsFragmentModule {
            return PostsFragmentModule(fragment)
        }
    }
}

class ArticlesViewModelProvider(private val fragment: PostsFragment) : Provider<ArticlesViewModel> {

    private val manager by inject<HabrArticleManager>()
    private val sessionDao by inject<SessionDao>()
    private val articleDao by inject<ArticleDao>()

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)
    }

    override fun get(): ArticlesViewModel {
        val factory = ArticlesViewModel.Factory(sessionDao, manager)
        return ViewModelProviders.of(fragment, factory)[ArticlesViewModel::class.java]
    }

}
