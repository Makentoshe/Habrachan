package com.makentoshe.habrachan.application.android.screen.comments.di

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.arena.CommentsArenaCache
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.ArticleCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.navigation.ArticleCommentsNavigation
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.ArticleCommentsViewModel
import com.makentoshe.habrachan.application.core.arena.comments.CommentsSourceFirstArena
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.CommentsManager
import okhttp3.OkHttpClient
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject
import javax.inject.Qualifier

@Qualifier
annotation class ArticleCommentsScope
class ArticleCommentsModule(fragment: ArticleCommentsFragment) : Module() {

    private val router by inject<Router>()
    private val client by inject<OkHttpClient>()
    private val session by inject<UserSession>()
    private val database by inject<AndroidCacheDatabase>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        val navigation =
            ArticleCommentsNavigation(router, fragment.arguments.articleId, fragment.arguments.articleTitle)
        bind<ArticleCommentsNavigation>().toInstance(navigation)

        val commentAdapter = CommentAdapter(navigation)
        bind<CommentAdapter>().toInstance(commentAdapter)

        val viewModel = getArticleCommentsViewModel(fragment)
        bind<ArticleCommentsViewModel>().toInstance(viewModel)
    }

    private fun getArticleCommentsViewModel(fragment: ArticleCommentsFragment): ArticleCommentsViewModel {
        val manager = CommentsManager.Factory(client).native()
        val arena = CommentsSourceFirstArena(manager, CommentsArenaCache(database.commentDao()))
        val factory = ArticleCommentsViewModel.Factory(session, arena)
        return ViewModelProviders.of(fragment, factory)[ArticleCommentsViewModel::class.java]
    }
}

