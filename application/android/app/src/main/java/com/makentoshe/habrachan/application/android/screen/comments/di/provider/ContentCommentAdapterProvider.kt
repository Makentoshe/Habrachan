package com.makentoshe.habrachan.application.android.screen.comments.di.provider

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.application.android.common.comment.BlockViewController
import com.makentoshe.habrachan.application.android.common.comment.CommentBodyController
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModelProvider
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.ContentCommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.AvatarCommentAdapterControllerDecorator
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.BottomPanelCommentAdapterControllerDecorator
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.ContentCommentAdapterController
import com.makentoshe.habrachan.application.android.screen.comments.navigation.CommentsNavigation
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.ArticleCommentsViewModel
import javax.inject.Provider

internal class ContentCommentAdapterProvider(
    private val fragment: Fragment,
    private val navigation: CommentsNavigation,
    private val voteCommentViewModelProvider: VoteCommentViewModelProvider,
    private val articleCommentsViewModel: ArticleCommentsViewModel,
): Provider<ContentCommentAdapter> {

    private val blockContentFactory = BlockViewController.BlockContent.Factory(fragment.requireContext())
    private val commentContentFactory = CommentBodyController.CommentContent.Factory(fragment.requireContext())

    override fun get(): ContentCommentAdapter {
        val avatarDecorator = AvatarCommentAdapterControllerDecorator(null, fragment.lifecycleScope, articleCommentsViewModel)
        return contentCommentAdapterChain2(avatarDecorator)
    }

    private fun contentCommentAdapterChain2(avatarDecorator: AvatarCommentAdapterControllerDecorator): ContentCommentAdapter {
        val bottomPanelDecorator = BottomPanelCommentAdapterControllerDecorator(avatarDecorator, fragment.lifecycleScope, voteCommentViewModelProvider)
        return contentCommentAdapterChain3(bottomPanelDecorator)
    }

    private fun contentCommentAdapterChain3(bottomPanelDecorator: BottomPanelCommentAdapterControllerDecorator): ContentCommentAdapter {
        val commentContentFactory = commentContentFactory.setNavigationOnImageClick(navigation)
        val controller = ContentCommentAdapterController(bottomPanelDecorator, commentContentFactory)
        return contentCommentAdapterChain4(controller)
    }

    private fun contentCommentAdapterChain4(contentCommentAdapterController: ContentCommentAdapterController): ContentCommentAdapter {
        val blockContentFactory = blockContentFactory.setNavigation(navigation)
        return ContentCommentAdapter(contentCommentAdapterController, blockContentFactory, navigation)
    }

}