package com.makentoshe.habrachan.application.android.screen.comments.di

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.ExceptionHandler
import com.makentoshe.habrachan.application.android.arena.CommentsArenaCache
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.ArticleCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentAdapter2
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.ArticleCommentsViewModel
import com.makentoshe.habrachan.application.core.arena.comments.CommentsArena
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

class ArticleCommentsModule(fragment: ArticleCommentsFragment): Module() {

    private val router by inject<Router>()
    private val client by inject<OkHttpClient>()
    private val session by inject<UserSession>()
    private val exceptionHandler by inject<ExceptionHandler>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        val adapter = CommentAdapter2(fragment.childFragmentManager)
        bind<CommentAdapter2>().toInstance(adapter)

        val viewModel = getArticleCommentsViewModel(fragment)
        bind<ArticleCommentsViewModel>().toInstance(viewModel)
    }

    private fun getArticleCommentsViewModel(fragment: ArticleCommentsFragment): ArticleCommentsViewModel {
        val manager = CommentsManager.Factory(client).native()
        val arena = CommentsArena(manager, CommentsArenaCache())
        val factory = ArticleCommentsViewModel.Factory(session, arena)
        return ViewModelProviders.of(fragment, factory)[ArticleCommentsViewModel::class.java]
    }
}

