package com.makentoshe.habrachan.application.android.screen.comments.di

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.arena.CommentsArenaCache
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.ArticleCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.model.ReplyCommentPagingAdapter
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.CommentsViewModel
import com.makentoshe.habrachan.application.core.arena.comments.CommentsSourceFirstArena
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.CommentsManager
import okhttp3.OkHttpClient
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject
import javax.inject.Qualifier

@Qualifier
annotation class ArticleCommentsScope

class ArticleCommentsModule(fragment: ArticleCommentsFragment): Module() {

    private val client by inject<OkHttpClient>()
    private val session by inject<UserSession>()
    private val database by inject<AndroidCacheDatabase>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        val adapter = ReplyCommentPagingAdapter(fragment.childFragmentManager, fragment.arguments.articleId)
        bind<ReplyCommentPagingAdapter>().toInstance(adapter)

        val viewModel = getArticleCommentsViewModel(fragment)
        bind<CommentsViewModel>().toInstance(viewModel)
    }

    private fun getArticleCommentsViewModel(fragment: ArticleCommentsFragment): CommentsViewModel {
        val manager = CommentsManager.Factory(client).native()
        val arena = CommentsSourceFirstArena(manager, CommentsArenaCache(database.commentDao()))
        val factory = CommentsViewModel.Factory(session, arena)
        return ViewModelProviders.of(fragment, factory)[CommentsViewModel::class.java]
    }
}

