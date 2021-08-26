package com.makentoshe.habrachan.application.android.screen.comments.articles.di.provider

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModel
import com.makentoshe.habrachan.application.android.common.comment.controller.block.body.content.ContentBodyBlock
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.body.content.ContentBodyComment
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModelProvider
import com.makentoshe.habrachan.application.android.common.navigation.navigator.ContentScreenNavigator
import com.makentoshe.habrachan.application.android.common.navigation.navigator.DispatchCommentsScreenNavigator
import com.makentoshe.habrachan.application.android.common.navigation.navigator.ThreadCommentsScreenNavigator
import com.makentoshe.habrachan.application.android.common.navigation.navigator.UserScreenNavigator
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.ContentCommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.AvatarCommentAdapterControllerDecorator
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.BottomPanelCommentAdapterControllerDecorator
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.ContentCommentAdapterController
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
    lateinit var userScreenNavigator: UserScreenNavigator

    @Inject
    lateinit var threadCommentsScreenNavigator: ThreadCommentsScreenNavigator

    @Inject
    lateinit var dispatchCommentsScreenNavigator: DispatchCommentsScreenNavigator

    @Inject
    lateinit var contentScreenNavigator: ContentScreenNavigator

    @Inject
    lateinit var voteCommentViewModelProvider: VoteCommentViewModelProvider

    @Inject
    internal lateinit var getAvatarViewModel: GetAvatarViewModel

    override fun get(): ContentCommentAdapter {
        val avatarDecorator = AvatarCommentAdapterControllerDecorator(null, fragment.lifecycleScope, getAvatarViewModel)
        return contentCommentAdapterChain2(avatarDecorator)
    }

    private fun contentCommentAdapterChain2(avatarDecorator: AvatarCommentAdapterControllerDecorator): ContentCommentAdapter {
        val bottomPanelDecorator = BottomPanelCommentAdapterControllerDecorator(avatarDecorator, fragment.lifecycleScope, voteCommentViewModelProvider, dispatchCommentsScreenNavigator)
        return contentCommentAdapterChain3(bottomPanelDecorator)
    }

    private fun contentCommentAdapterChain3(bottomPanelDecorator: BottomPanelCommentAdapterControllerDecorator): ContentCommentAdapter {
        val commentContentFactory = contentBodyCommentFactory.setNavigationOnImageClick(contentScreenNavigator)
        val controller = ContentCommentAdapterController(bottomPanelDecorator, commentContentFactory)
        return contentCommentAdapterChain4(controller)
    }

    private fun contentCommentAdapterChain4(contentCommentAdapterController: ContentCommentAdapterController): ContentCommentAdapter {
        val blockContentFactory = contentBodyBlockFactory.setNavigation(threadCommentsScreenNavigator)
        return ContentCommentAdapter(contentCommentAdapterController, blockContentFactory, userScreenNavigator)
    }

}