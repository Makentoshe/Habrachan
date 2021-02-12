package com.makentoshe.habrachan.application.android.screen.comments.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.makentoshe.habrachan.application.android.arena.AvatarArenaCache
import com.makentoshe.habrachan.application.android.arena.CommentsArenaCache
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.DiscussionCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.navigation.ArticleCommentsNavigation
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.DiscussionCommentsViewModel
import com.makentoshe.habrachan.application.core.arena.comments.CommentsCacheFirstArena
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
annotation class DiscussionCommentsScope

internal const val TitleAdapterQualifier = "TitleAdapter"

internal const val CommentsAdapterQualifier = "CommentsAdapter"

class DiscussionCommentsModule(fragment: DiscussionCommentsFragment): Module() {

    private val router by inject<Router>()
    private val client by inject<OkHttpClient>()
    private val session by inject<UserSession>()
    private val database by inject<AndroidCacheDatabase>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        val viewModel = getDiscussionCommentsViewModel(fragment)
        bind<DiscussionCommentsViewModel>().toInstance(viewModel)

        val navigation =
            ArticleCommentsNavigation(router, fragment.arguments.articleId, fragment.arguments.articleTitle)
        bind<ArticleCommentsNavigation>().toInstance(navigation)

        val commentsAdapter = CommentAdapter(navigation, fragment.lifecycleScope, viewModel)
        bind<CommentAdapter>().withName(CommentsAdapterQualifier).toInstance(commentsAdapter)

        val titleAdapter = CommentAdapter(navigation, fragment.lifecycleScope, viewModel)
        bind<CommentAdapter>().withName(TitleAdapterQualifier).toInstance(titleAdapter)

        bind<ConcatAdapter>().toInstance(ConcatAdapter(titleAdapter, commentsAdapter))

    }

    private fun getDiscussionCommentsViewModel(fragment: Fragment): DiscussionCommentsViewModel {
        val avatarCache = AvatarArenaCache(database.avatarDao(), fragment.requireContext().cacheDir)
        val avatarArena = ImageArena(ImageManager.Builder(client).build(), avatarCache)

        val commentsManager = CommentsManager.Factory(client).native()
        val commentsArena = CommentsCacheFirstArena(commentsManager, CommentsArenaCache(database.commentDao()))

        val factory = DiscussionCommentsViewModel.Factory(session, commentsArena, avatarArena)
        return ViewModelProviders.of(fragment, factory)[DiscussionCommentsViewModel::class.java]
    }

}