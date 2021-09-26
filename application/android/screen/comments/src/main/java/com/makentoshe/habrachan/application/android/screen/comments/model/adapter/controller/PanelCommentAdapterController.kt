package com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.makentoshe.habrachan.application.android.common.collectResult
import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.CommentViewController
import com.makentoshe.habrachan.application.android.common.comment.model.forest.CommentModelElement
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentSpec
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModel
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModelProvider
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.DispatchCommentsScreenNavigator
import com.makentoshe.habrachan.entity.CommentVote
import com.makentoshe.habrachan.entity.commentId
import com.makentoshe.habrachan.network.NativeVoteCommentException
import com.makentoshe.habrachan.network.response.VoteCommentResponse2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class PanelCommentAdapterController(
    private val lifecycleScope: CoroutineScope,
    private val voteCommentViewModelProvider: VoteCommentViewModelProvider,
    private val dispatchCommentsScreenNavigator: DispatchCommentsScreenNavigator,
    private val installWizard: InstallWizard = InstallWizard()
) : CommentAdapterController {


    private fun CommentModelElement.getViewModel(): VoteCommentViewModel {
        return voteCommentViewModelProvider.get(comment.commentId.toString())
    }

    override fun onBindViewHolderComment(holder: CommentViewHolder, model: CommentModelElement) {
        val commentViewController = CommentViewController(holder).apply { panel.showExpanded() }
        commentViewController.installPanelState(installWizard)

        commentViewController.panel.vote.setCurrentVoteState(model.comment)
        commentViewController.panel.vote.voteScore.setVoteScore(model.comment)

        commentViewController.panel.vote.voteUp.setVoteUpAction(lifecycleScope) {
            model.getViewModel().voteCommentChannel.send(VoteCommentSpec(model.comment, CommentVote.Up))
        }
        commentViewController.panel.vote.voteDown.setVoteDownAction(lifecycleScope) {
            model.getViewModel().voteCommentChannel.send(VoteCommentSpec(model.comment, CommentVote.Down))
        }
        commentViewController.panel.reply.setReplyAction(lifecycleScope, Dispatchers.Main) {
            dispatchCommentsScreenNavigator.toDispatchScreen(model.articleId, commentId(model.comment.commentId))
        }

        model.getViewModel().voteCommentFlow.collectResult(lifecycleScope, { response ->
            onVoteCommentSuccess(holder, response)
        }, { throwable ->
            onVoteCommentFailure(holder, throwable)
        })
    }

    private fun CommentViewController.installPanelState(installWizard: InstallWizard) = when (installWizard.panelState) {
        InstallWizard.PanelState.EXPANDED -> panel.showExpanded()
        InstallWizard.PanelState.COLLAPSED -> panel.showCollapsed()
        InstallWizard.PanelState.HIDDEN -> panel.showHidden()
    }

    private fun onVoteCommentSuccess(holder: CommentViewHolder, response: VoteCommentResponse2) {
        val controller = CommentViewController(holder)
        controller.panel.vote.voteScore.setVoteScore(response.score)
        when (response.request.commentVote) {
            CommentVote.Up -> controller.panel.vote.voteUp.setVoteUpIcon()
            CommentVote.Down -> controller.panel.vote.voteDown.setVoteDownIcon()
        }
    }

    private fun onVoteCommentFailure(holder: CommentViewHolder, throwable: Throwable) {
        val parentView = holder.itemView.parent as View
        val message = if (throwable is NativeVoteCommentException) {
            throwable.additional.joinToString(". ")
        } else {
            throwable.message ?: throwable.toString()
        }
        Snackbar.make(parentView, message, Snackbar.LENGTH_LONG).show()
    }

    class InstallWizard(
        var panelState: PanelState = PanelState.EXPANDED
    ) {

        enum class PanelState {
            COLLAPSED, EXPANDED, HIDDEN
        }
    }
}