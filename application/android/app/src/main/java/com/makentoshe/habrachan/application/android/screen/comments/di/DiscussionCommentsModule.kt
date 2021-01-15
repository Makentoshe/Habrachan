package com.makentoshe.habrachan.application.android.screen.comments.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.arena.CommentsArenaCache
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.DiscussionCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.navigation.ArticleCommentsNavigation
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.DiscussionCommentsViewModel
import com.makentoshe.habrachan.application.core.arena.comments.CommentsCacheFirstArena
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
annotation class DiscussionCommentsScope
class DiscussionCommentsModule(fragment: DiscussionCommentsFragment): Module() {

    private val router by inject<Router>()
    private val client by inject<OkHttpClient>()
    private val session by inject<UserSession>()
    private val database by inject<AndroidCacheDatabase>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        val navigation =
            ArticleCommentsNavigation(router, fragment.arguments.articleId, fragment.arguments.articleTitle)
        bind<ArticleCommentsNavigation>().toInstance(navigation)

        bind<CommentAdapter>().toInstance(CommentAdapter(navigation))

        val viewModel = getDiscussionCommentsViewModel(fragment)
        bind<DiscussionCommentsViewModel>().toInstance(viewModel)
    }

    private fun getDiscussionCommentsViewModel(fragment: Fragment): DiscussionCommentsViewModel {
        val manager = CommentsManager.Factory(client).native()
        val arena = CommentsCacheFirstArena(manager, CommentsArenaCache(database.commentDao()))
        val factory = DiscussionCommentsViewModel.Factory(session, arena)
        return ViewModelProviders.of(fragment, factory)[DiscussionCommentsViewModel::class.java]
    }

}