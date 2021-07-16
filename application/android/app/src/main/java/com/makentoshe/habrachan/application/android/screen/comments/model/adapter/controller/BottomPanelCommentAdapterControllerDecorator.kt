package com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller

import android.widget.Toast
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.common.comment.CommentBottomPanelController
import com.makentoshe.habrachan.application.android.common.comment.CommentViewController
import com.makentoshe.habrachan.application.android.common.comment.CommentViewControllerNavigator
import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentSpec
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModel
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.VoteCommentViewModelProvider
import com.makentoshe.habrachan.application.android.common.core.collectResult
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentModelElement
import com.makentoshe.habrachan.network.request.CommentVote
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class BottomPanelCommentAdapterControllerDecorator(
    private val commentAdapterController: CommentAdapterController?,
    private val lifecycleScope: CoroutineScope,
    private val navigation: CommentViewControllerNavigator,
    private val voteCommentViewModelProvider: VoteCommentViewModelProvider,
    private val additional: (CommentBottomPanelController) -> Unit = { }
): CommentAdapterController {

    private fun CommentModelElement.getViewModel(): VoteCommentViewModel {
        return voteCommentViewModelProvider.get(comment.commentId)
    }

    override fun onBindViewHolderComment(holder: CommentViewHolder, position: Int, model: CommentModelElement) {
        commentAdapterController?.onBindViewHolderComment(holder, position, model)
        internalOnBindViewHolderBottomPanel(holder, model)

        model.getViewModel().voteCommentFlow.collectResult(lifecycleScope, {
            CommentViewController(holder).setVoteScore(it.score)
        }, {
            println(it)
        })
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