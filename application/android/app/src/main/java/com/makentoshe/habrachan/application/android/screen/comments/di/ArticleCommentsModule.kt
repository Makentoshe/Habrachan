package com.makentoshe.habrachan.application.android.screen.comments.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.application.android.arena.AvatarArenaCache
import com.makentoshe.habrachan.application.android.arena.CommentsArenaCache
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.ArticleCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.navigation.CommentsNavigation
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.ArticleCommentsViewModel
import com.makentoshe.habrachan.application.core.arena.comments.CommentsSourceFirstArena
import com.makentoshe.habrachan.application.core.arena.image.ContentArena
import com.makentoshe.habrachan.network.manager.GetArticleCommentsManager
import com.makentoshe.habrachan.network.manager.GetContentManager
import com.makentoshe.habrachan.network.request.GetArticleCommentsRequest
import toothpick.Toothpick
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject
import javax.inject.Qualifier

@Qualifier
annotation class ArticleCommentsScope
class ArticleCommentsModule(fragment: ArticleCommentsFragment) : CommentsModule(fragment) {

    private val getContentManager by inject<GetContentManager>()
    private val getCommentsManager by inject<GetArticleCommentsManager<GetArticleCommentsRequest>>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        val viewModel = getArticleCommentsViewModel(fragment)
        bind<ArticleCommentsViewModel>().toInstance(viewModel)

        val navigation = CommentsNavigation(router, fragment.arguments.articleId, fragment.arguments.articleTitle)
        bind<CommentsNavigation>().toInstance(navigation)

        val commentAdapter = getCommentAdapter(fragment, viewModel, navigation)
        bind<CommentAdapter>().toInstance(commentAdapter)
    }

    private fun getCommentAdapter(fragment: Fragment, viewModel: ArticleCommentsViewModel, navigation: CommentsNavigation): CommentAdapter {
        val commentContentFactory = commentContentFactory.setNavigationOnImageClick(navigation)
        val blockContentFactory = blockContentFactory.setNavigation(navigation)
        return CommentAdapter(fragment.lifecycleScope, viewModel, commentContentFactory, blockContentFactory)
    }

    private fun getArticleCommentsViewModel(fragment: ArticleCommentsFragment): ArticleCommentsViewModel {
        val avatarCache = AvatarArenaCache(database.avatarDao(), fragment.requireContext().cacheDir)
        val avatarArena = ContentArena(getContentManager, avatarCache)

        val commentsArena = CommentsSourceFirstArena(getCommentsManager, CommentsArenaCache(database.commentDao()))

        val factory = ArticleCommentsViewModel.Factory(session, commentsArena, avatarArena)
        return ViewModelProviders.of(fragment, factory)[ArticleCommentsViewModel::class.java]
    }
}

