package com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.comment.controller.comment.CommentViewController
import com.makentoshe.habrachan.application.android.common.comment.model.forest.CommentModelElement
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentSpec
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModel
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModelProvider
import com.makentoshe.habrachan.application.android.common.core.collectResult
import com.makentoshe.habrachan.application.android.common.navigation.navigator.DispatchCommentsScreenNavigator
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
) : CommentAdapterController {

    private fun CommentModelElement.getViewModel(): VoteCommentViewModel {
        return voteCommentViewModelProvider.get(comment.commentId.toString())
    }

    override fun onBindViewHolderComment(holder: CommentViewHolder, model: CommentModelElement) {
        val commentViewController = CommentViewController(holder)
        commentViewController.panel.vote.setCurrentVoteState(model.comment)

        commentViewController.panel.vote.voteUp.setVoteUpAction(lifecycleScope) {
            model.getViewModel().voteCommentChannel.send(VoteCommentSpec(model.comment, CommentVote.Up))
        }
        commentViewController.panel.vote.voteDown.setVoteDownAction(lifecycleScope) {
            model.getViewModel().voteCommentChannel.send(VoteCommentSpec(model.comment, CommentVote.Down))
        }
        commentViewController.panel.reply.setReplyAction(lifecycleScope, Dispatchers.Main) {
            dispatchCommentsScreenNavigator.toDispatchScreen(commentId(model.comment.commentId))
        }

        model.getViewModel().voteCommentFlow.collectResult(lifecycleScope, { response ->
            onVoteCommentSuccess(holder, response)
        }, { throwable ->
            onVoteCommentFailure(holder, throwable)
        })
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
}