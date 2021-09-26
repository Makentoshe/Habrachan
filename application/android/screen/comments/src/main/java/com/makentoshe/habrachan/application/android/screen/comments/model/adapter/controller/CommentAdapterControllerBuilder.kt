package com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModelProvider
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.body.content.ContentBodyComment
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModelProvider
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.DispatchCommentsScreenNavigator
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class CommentAdapterControllerBuilder @Inject constructor(
    private val lifecycleScope: CoroutineScope,
    private val getAvatarViewModelProvider: GetAvatarViewModelProvider,
    private val contentBodyCommentFactory: ContentBodyComment.Factory,
    private var voteCommentViewModelProvider: VoteCommentViewModelProvider,
    private var dispatchCommentsScreenNavigator: DispatchCommentsScreenNavigator,
) {

    fun build(
        fragment: Fragment,
        bodyInstallWizard: BodyCommentAdapterController.InstallWizard,
        panelInstallWizard: PanelCommentAdapterController.InstallWizard,
    ): ComposeCommentAdapterController {
        return ComposeCommentAdapterController(buildAvatar(fragment), buildBody(bodyInstallWizard), buildPanel(panelInstallWizard))
    }

    fun buildAvatar(fragment: Fragment): AvatarCommentAdapterController {
        val getAvatarViewModel = getAvatarViewModelProvider.get(fragment)
        return AvatarCommentAdapterController(lifecycleScope, getAvatarViewModel)
    }

    fun buildBody(installWizard: BodyCommentAdapterController.InstallWizard): BodyCommentAdapterController {
        return BodyCommentAdapterController(contentBodyCommentFactory, installWizard)
    }

    fun buildPanel(installWizard: PanelCommentAdapterController.InstallWizard): PanelCommentAdapterController {
        return PanelCommentAdapterController(lifecycleScope, voteCommentViewModelProvider, dispatchCommentsScreenNavigator, installWizard)
    }

}