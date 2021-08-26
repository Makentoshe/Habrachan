package com.makentoshe.habrachan.application.android.screen.comments.discussion.di.provider

import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModel
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.body.content.ContentBodyComment
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModelProvider
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.TitleCommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.AvatarCommentAdapterControllerDecorator
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.BottomPanelCommentAdapterControllerDecorator
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.ContentCommentAdapterController
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Provider

internal class TitleCommentAdapterProvider : Provider<TitleCommentAdapter> {

    @Inject
    internal lateinit var contentBodyCommentFactory: ContentBodyComment.Factory

    @Inject
    internal lateinit var voteCommentViewModelProvider: VoteCommentViewModelProvider

    @Inject
    internal lateinit var fragmentLifecycleScope: CoroutineScope

    @Inject
    internal lateinit var getAvatarViewModel: GetAvatarViewModel

    override fun get(): TitleCommentAdapter {
        val avatar = AvatarCommentAdapterControllerDecorator(null, fragmentLifecycleScope, getAvatarViewModel)
        val bottomPanel = BottomPanelCommentAdapterControllerDecorator(avatar, fragmentLifecycleScope, voteCommentViewModelProvider)
        return TitleCommentAdapter(ContentCommentAdapterController(bottomPanel, contentBodyCommentFactory) { controller ->
            controller.body.collapse()
        })
    }
}