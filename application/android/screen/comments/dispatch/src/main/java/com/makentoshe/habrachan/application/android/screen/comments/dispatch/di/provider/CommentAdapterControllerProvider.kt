package com.makentoshe.habrachan.application.android.screen.comments.dispatch.di.provider

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.BodyCommentAdapterController
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.CommentAdapterController
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.CommentAdapterControllerBuilder
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.PanelCommentAdapterController
import javax.inject.Inject
import javax.inject.Provider

internal class CommentAdapterControllerProvider @Inject constructor(
    private val fragment: Fragment,
    private val commentAdapterControllerBuilder: CommentAdapterControllerBuilder,
) : Provider<CommentAdapterController> {

    override fun get(): CommentAdapterController {
        return commentAdapterControllerBuilder.build(fragment, bodyInstallWizard(), panelInstallWizard())
    }

    private fun bodyInstallWizard() = BodyCommentAdapterController.InstallWizard(
        bodyState = BodyCommentAdapterController.InstallWizard.BodyState.EXPANDED
    )

    private fun panelInstallWizard() = PanelCommentAdapterController.InstallWizard(
        panelState = PanelCommentAdapterController.InstallWizard.PanelState.HIDDEN
    )
}