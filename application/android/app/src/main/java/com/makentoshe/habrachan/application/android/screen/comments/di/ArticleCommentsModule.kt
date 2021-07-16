package com.makentoshe.habrachan.application.android.screen.comments.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.application.android.arena.AvatarArenaCache
import com.makentoshe.habrachan.application.android.arena.CommentsArenaCache
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.ArticleCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.ContentCommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.AvatarCommentAdapterControllerDecorator
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.BottomPanelCommentAdapterControllerDecorator
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.ContentCommentAdapterController
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

        val navigation = CommentsNavigation(router, fragment.arguments.articleId, fragment.arguments.articleTitle, fragment.childFragmentManager)
        bind<CommentsNavigation>().toInstance(navigation)

        val commentAdapter = getContentCommentAdapter(fragment, viewModel, navigation)
        bind<ContentCommentAdapter>().toInstance(commentAdapter)
    }

    private fun getContentCommentAdapter(
        fragment: Fragment, viewModel: ArticleCommentsViewModel, navigation: CommentsNavigation
    ): ContentCommentAdapter {
        val commentContentFactory = commentContentFactory.setNavigationOnImageClick(navigation)
        val avatarDecorator = AvatarCommentAdapterControllerDecorator(null, fragment.lifecycleScope, viewModel)
        val bottomPanelDecorator = BottomPanelCommentAdapterControllerDecorator(avatarDecorator, fragment.lifecycleScope, voteCommentViewModelProvider)
        val contentCommentAdapterController = ContentCommentAdapterController(bottomPanelDecorator, commentContentFactory)
        return ContentCommentAdapter(contentCommentAdapterController, blockContentFactory.setNavigation(navigation))
    }

    private fun getArticleCommentsViewModel(fragment: ArticleCommentsFragment): ArticleCommentsViewModel {
        val avatarCache = AvatarArenaCache(database.avatarDao(), fragment.requireContext().cacheDir)
        val avatarArena = ContentArena(getContentManager, avatarCache)

        val commentsArena = CommentsSourceFirstArena(getCommentsManager, CommentsArenaCache(database.commentDao()))

        val factory = ArticleCommentsViewModel.Factory(session, commentsArena, avatarArena)
        return ViewModelProviders.of(fragment, factory)[ArticleCommentsViewModel::class.java]
    }
}

