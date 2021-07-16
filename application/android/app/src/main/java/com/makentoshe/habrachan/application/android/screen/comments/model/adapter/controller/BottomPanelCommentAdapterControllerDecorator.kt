package com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller

import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.common.comment.CommentBottomPanelController
import com.makentoshe.habrachan.application.android.common.comment.CommentViewController
import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentSpec
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModel
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModelProvider
import com.makentoshe.habrachan.application.android.common.core.collectResult
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentModelElement
import com.makentoshe.habrachan.entity.CommentVote
import com.makentoshe.habrachan.network.NativeVoteCommentException
import com.makentoshe.habrachan.network.response.VoteCommentResponse2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class BottomPanelCommentAdapterControllerDecorator(
    private val commentAdapterController: CommentAdapterController?,
    private val lifecycleScope: CoroutineScope,
    private val voteCommentViewModelProvider: VoteCommentViewModelProvider,
    private val additional: (CommentBottomPanelController) -> Unit = { }
) : CommentAdapterController {

    private fun CommentModelElement.getViewModel(): VoteCommentViewModel {
        return voteCommentViewModelProvider.get(comment.commentId)
    }

    override fun onBindViewHolderComment(holder: CommentViewHolder, position: Int, model: CommentModelElement) {
        commentAdapterController?.onBindViewHolderComment(holder, position, model)
        internalOnBindViewHolderBottomPanel(holder, model)

        model.getViewModel().voteCommentFlow.collectResult(lifecycleScope, {
            onVoteCommentSuccess(holder, it)
        }, {
            onVoteCommentFailure(holder, it)
        })
    }

    private fun onVoteCommentSuccess(holder: CommentViewHolder, response: VoteCommentResponse2) {
        val controller = CommentViewController(holder).setVoteScore(response.score)
        when (response.request.commentVote) {
            CommentVote.Up -> controller.setVoteUpIcon()
            CommentVote.Down -> controller.setVoteDownIcon()
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

    private fun internalOnBindViewHolderBottomPanel(holder: CommentViewHolder, model: CommentModelElement) {
        with(CommentBottomPanelController(holder).showExpanded()) {
            setVoteUpAction(lifecycleScope) {
                model.getViewModel().voteCommentChannel.send(VoteCommentSpec(model.comment.commentId, CommentVote.Up))
            }
            setVoteDownAction(lifecycleScope) {
                model.getViewModel().voteCommentChannel.send(VoteCommentSpec(model.comment.commentId, CommentVote.Down))
            }

            setBookmarkAction(lifecycleScope, Dispatchers.Main) {
                Toast.makeText(holder.context, R.string.not_implemented, Toast.LENGTH_SHORT).show()
            }
            setReplyAction(lifecycleScope, Dispatchers.Main) {
                Toast.makeText(holder.context, R.string.not_implemented, Toast.LENGTH_SHORT).show()
            }
            setShareAction(lifecycleScope, Dispatchers.Main) {
                Toast.makeText(holder.context, R.string.not_implemented, Toast.LENGTH_SHORT).show()
            }
            setOverflowAction(lifecycleScope, Dispatchers.Main) {
                Toast.makeText(holder.context, R.string.not_implemented, Toast.LENGTH_SHORT).show()
            }

            apply(additional)
        }
    }
}