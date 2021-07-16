package com.makentoshe.habrachan.application.android.screen.comments.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.makentoshe.habrachan.application.android.arena.AvatarArenaCache
import com.makentoshe.habrachan.application.android.arena.CommentsArenaCache
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.DiscussionCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.ContentCommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.TitleCommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.AvatarCommentAdapterControllerDecorator
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.BottomPanelCommentAdapterControllerDecorator
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.ContentCommentAdapterController
import com.makentoshe.habrachan.application.android.screen.comments.navigation.CommentsNavigation
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.DiscussionCommentsViewModel
import com.makentoshe.habrachan.application.core.arena.comments.CommentsCacheFirstArena
import com.makentoshe.habrachan.application.core.arena.image.ContentArena
import com.makentoshe.habrachan.network.manager.GetArticleCommentsManager
import com.makentoshe.habrachan.network.manager.GetContentManager
import com.makentoshe.habrachan.network.request.GetArticleCommentsRequest
import toothpick.Toothpick
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject
import javax.inject.Qualifier

@Qualifier
annotation class DiscussionCommentsScope
class DiscussionCommentsModule(fragment: DiscussionCommentsFragment) : CommentsModule(fragment) {

    private val getContentManager by inject<GetContentManager>()
    private val getCommentsManager by inject<GetArticleCommentsManager<GetArticleCommentsRequest>>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        val viewModel = getDiscussionCommentsViewModel(fragment)
        bind<DiscussionCommentsViewModel>().toInstance(viewModel)

        val navigation = CommentsNavigation(
            router, fragment.arguments.articleId, fragment.arguments.articleTitle, fragment.childFragmentManager
        )
        bind<CommentsNavigation>().toInstance(navigation)

        val commentsAdapter = getCommentAdapter(fragment, viewModel, navigation)
        bind<ContentCommentAdapter>().toInstance(commentsAdapter)

        val titleAdapter = getTitleAdapter(fragment, viewModel, navigation)
        bind<TitleCommentAdapter>().toInstance(titleAdapter)

        bind<ConcatAdapter>().toInstance(ConcatAdapter(titleAdapter, commentsAdapter))
    }

    private fun getCommentAdapter(
        fragment: Fragment, viewModel: DiscussionCommentsViewModel, navigation: CommentsNavigation
    ): ContentCommentAdapter {
        val commentContentFactory = commentContentFactory.setNavigationOnImageClick(navigation)
        val blockContentFactory = blockContentFactory.setNavigation(navigation)
        val avatarDecorator = AvatarCommentAdapterControllerDecorator(null, fragment.lifecycleScope, viewModel)
        val bottomPanelDecorator = BottomPanelCommentAdapterControllerDecorator(avatarDecorator, fragment.lifecycleScope, voteCommentViewModelProvider)
        val contentCommentAdapterController = ContentCommentAdapterController(bottomPanelDecorator, commentContentFactory)
        return ContentCommentAdapter(contentCommentAdapterController, blockContentFactory.setNavigation(navigation))
    }

    private fun getTitleAdapter(
        fragment: Fragment, viewModel: DiscussionCommentsViewModel, navigation: CommentsNavigation
    ): TitleCommentAdapter {
        val commentContentFactory = commentContentFactory.setNavigationOnImageClick(navigation)

        val avatarDecorator = AvatarCommentAdapterControllerDecorator(null, fragment.lifecycleScope, viewModel)
        val bottomPanelDecorator = BottomPanelCommentAdapterControllerDecorator(
            avatarDecorator, fragment.lifecycleScope,  voteCommentViewModelProvider
        ) { bottomPanelController -> bottomPanelController.hide() }
        val contentCommentAdapterController = ContentCommentAdapterController(
            bottomPanelDecorator, commentContentFactory
        ) { _, bodyController -> bodyController.collapse() }
        return TitleCommentAdapter(contentCommentAdapterController)
    }

    private fun getDiscussionCommentsViewModel(fragment: Fragment): DiscussionCommentsViewModel {
        val avatarCache = AvatarArenaCache(database.avatarDao(), fragment.requireContext().cacheDir)
        val avatarArena = ContentArena(getContentManager, avatarCache)

        val commentsArena = CommentsCacheFirstArena(getCommentsManager, CommentsArenaCache(database.commentDao()))

        val factory = DiscussionCommentsViewModel.Factory(session, commentsArena, avatarArena)
        return ViewModelProviders.of(fragment, factory)[DiscussionCommentsViewModel::class.java]
    }

}