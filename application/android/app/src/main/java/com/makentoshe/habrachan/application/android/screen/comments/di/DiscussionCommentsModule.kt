package com.makentoshe.habrachan.application.android.screen.comments.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.makentoshe.habrachan.application.android.arena.AvatarArenaCache
import com.makentoshe.habrachan.application.android.arena.CommentsArenaCache
import com.makentoshe.habrachan.application.android.common.comment.CommentViewController
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.DiscussionCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentAdapter
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

internal const val TitleAdapterQualifier = "TitleAdapter"

internal const val CommentsAdapterQualifier = "CommentsAdapter"

class DiscussionCommentsModule(fragment: DiscussionCommentsFragment) : CommentsModule(fragment) {

    private val getContentManager by inject<GetContentManager>()
    private val getCommentsManager by inject<GetArticleCommentsManager<GetArticleCommentsRequest>>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        val viewModel = getDiscussionCommentsViewModel(fragment)
        bind<DiscussionCommentsViewModel>().toInstance(viewModel)

        val navigation = CommentsNavigation(router, fragment.arguments.articleId, fragment.arguments.articleTitle, fragment.childFragmentManager)
        bind<CommentsNavigation>().toInstance(navigation)

        val commentsAdapter = getCommentAdapter(fragment, viewModel, navigation)
        bind<CommentAdapter>().withName(CommentsAdapterQualifier).toInstance(commentsAdapter)

        val titleAdapter = getTitleAdapter(fragment, viewModel, navigation)
        bind<CommentAdapter>().withName(TitleAdapterQualifier).toInstance(titleAdapter)

        bind<ConcatAdapter>().toInstance(ConcatAdapter(titleAdapter, commentsAdapter))

    }

    private fun getCommentAdapter(fragment: Fragment, viewModel: DiscussionCommentsViewModel, navigation: CommentsNavigation): CommentAdapter {
        val commentControllerFactory = CommentViewController.Factory(navigation)
        val commentContentFactory = commentContentFactory.setNavigationOnImageClick(navigation)
        val blockContentFactory = blockContentFactory.setNavigation(navigation)
        return CommentAdapter(fragment.lifecycleScope, viewModel, commentControllerFactory, commentContentFactory, blockContentFactory)
    }

    private fun getTitleAdapter(fragment: Fragment, viewModel: DiscussionCommentsViewModel, navigation: CommentsNavigation): CommentAdapter {
        val commentControllerFactory = CommentViewController.Factory(navigation)
        val commentContentFactory = commentContentFactory.setNavigationOnImageClick(navigation)
        return CommentAdapter(fragment.lifecycleScope, viewModel, commentControllerFactory, commentContentFactory, blockContentFactory)
    }

    private fun getDiscussionCommentsViewModel(fragment: Fragment): DiscussionCommentsViewModel {
        val avatarCache = AvatarArenaCache(database.avatarDao(), fragment.requireContext().cacheDir)
        val avatarArena = ContentArena(getContentManager, avatarCache)

        val commentsArena = CommentsCacheFirstArena(getCommentsManager, CommentsArenaCache(database.commentDao()))

        val factory = DiscussionCommentsViewModel.Factory(session, commentsArena, avatarArena)
        return ViewModelProviders.of(fragment, factory)[DiscussionCommentsViewModel::class.java]
    }

}