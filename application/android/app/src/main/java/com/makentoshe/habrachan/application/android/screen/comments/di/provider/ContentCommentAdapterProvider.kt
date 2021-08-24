package com.makentoshe.habrachan.application.android.screen.comments.di.provider

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModel
import com.makentoshe.habrachan.application.android.common.comment.controller.block.body.content.ContentBodyBlock
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.body.content.ContentBodyComment
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModelProvider
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.ContentCommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.AvatarCommentAdapterControllerDecorator
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.BottomPanelCommentAdapterControllerDecorator
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.ContentCommentAdapterController
import com.makentoshe.habrachan.application.android.screen.comments.navigation.CommentsNavigation
import javax.inject.Inject
import javax.inject.Provider

internal class ContentCommentAdapterProvider : Provider<ContentCommentAdapter> {

    @Inject
    lateinit var contentBodyBlockFactory: ContentBodyBlock.Factory

    @Inject
    lateinit var contentBodyCommentFactory: ContentBodyComment.Factory

    @Inject
    lateinit var fragment: Fragment

    @Inject
    lateinit var navigation: CommentsNavigation

    @Inject
    lateinit var voteCommentViewModelProvider: VoteCommentViewModelProvider

    @Inject
    internal lateinit var getAvatarViewModel: GetAvatarViewModel

    override fun get(): ContentCommentAdapter {
        val avatarDecorator = AvatarCommentAdapterControllerDecorator(null, fragment.lifecycleScope, getAvatarViewModel)
        return contentCommentAdapterChain2(avatarDecorator)
    }

    private fun contentCommentAdapterChain2(avatarDecorator: AvatarCommentAdapterControllerDecorator): ContentCommentAdapter {
        val bottomPanelDecorator = BottomPanelCommentAdapterControllerDecorator(avatarDecorator, fragment.lifecycleScope, voteCommentViewModelProvider)
        return contentCommentAdapterChain3(bottomPanelDecorator)
    }

    private fun contentCommentAdapterChain3(bottomPanelDecorator: BottomPanelCommentAdapterControllerDecorator): ContentCommentAdapter {
        val commentContentFactory = contentBodyCommentFactory.setNavigationOnImageClick(navigation)
        val controller = ContentCommentAdapterController(bottomPanelDecorator, commentContentFactory)
        return contentCommentAdapterChain4(controller)
    }

    private fun contentCommentAdapterChain4(contentCommentAdapterController: ContentCommentAdapterController): ContentCommentAdapter {
        val blockContentFactory = contentBodyBlockFactory.setNavigation(navigation)
        return ContentCommentAdapter(contentCommentAdapterController, blockContentFactory, navigation)
    }

}