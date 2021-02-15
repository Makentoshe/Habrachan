package com.makentoshe.habrachan.application.android.screen.comments.di

import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.application.android.arena.AvatarArenaCache
import com.makentoshe.habrachan.application.android.arena.CommentsArenaCache
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.ArticleCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.navigation.CommentsNavigation
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.ArticleCommentsViewModel
import com.makentoshe.habrachan.application.core.arena.comments.CommentsSourceFirstArena
import com.makentoshe.habrachan.application.core.arena.image.ImageArena
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.CommentsManager
import com.makentoshe.habrachan.network.manager.ImageManager
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

        val viewModel = getArticleCommentsViewModel(fragment)
        bind<ArticleCommentsViewModel>().toInstance(viewModel)

        val navigation =
            CommentsNavigation(router, fragment.arguments.articleId, fragment.arguments.articleTitle)
        bind<CommentsNavigation>().toInstance(navigation)

        val commentAdapter = CommentAdapter(navigation, fragment.lifecycleScope, viewModel)
        bind<CommentAdapter>().toInstance(commentAdapter)

    }

    private fun getArticleCommentsViewModel(fragment: ArticleCommentsFragment): ArticleCommentsViewModel {
        val avatarCache = AvatarArenaCache(database.avatarDao(), fragment.requireContext().cacheDir)
        val avatarArena = ImageArena(ImageManager.Builder(client).build(), avatarCache)

        val commentsManager = CommentsManager.Factory(client).native()
        val commentsArena = CommentsSourceFirstArena(commentsManager, CommentsArenaCache(database.commentDao()))

        val factory = ArticleCommentsViewModel.Factory(session, commentsArena, avatarArena)
        return ViewModelProviders.of(fragment, factory)[ArticleCommentsViewModel::class.java]
    }
}

