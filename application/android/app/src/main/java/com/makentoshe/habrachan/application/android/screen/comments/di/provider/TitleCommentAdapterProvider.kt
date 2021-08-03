package com.makentoshe.habrachan.application.android.screen.comments.di.provider

import com.makentoshe.habrachan.application.android.common.comment.CommentBodyController
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModelProvider
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.TitleCommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.AvatarCommentAdapterControllerDecorator
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.BottomPanelCommentAdapterControllerDecorator
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.ContentCommentAdapterController
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.DiscussionCommentsViewModel
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Provider

internal class TitleCommentAdapterProvider : Provider<TitleCommentAdapter> {

    @Inject
    internal lateinit var commentContentFactory: CommentBodyController.CommentContent.Factory

    @Inject
    internal lateinit var voteCommentViewModelProvider: VoteCommentViewModelProvider

    @Inject
    internal lateinit var fragmentLifecycleScope: CoroutineScope

    @Inject
    internal lateinit var discussionCommentsViewModel: DiscussionCommentsViewModel

    override fun get(): TitleCommentAdapter {
        val avatar = AvatarCommentAdapterControllerDecorator(null, fragmentLifecycleScope, discussionCommentsViewModel)
        val bottomPanel = BottomPanelCommentAdapterControllerDecorator(avatar, fragmentLifecycleScope, voteCommentViewModelProvider)
        return TitleCommentAdapter(ContentCommentAdapterController(bottomPanel, commentContentFactory) { _, bodyController ->
            bodyController.collapse()
        })
    }

}