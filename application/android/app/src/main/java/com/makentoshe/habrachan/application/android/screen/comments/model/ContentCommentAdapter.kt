package com.makentoshe.habrachan.application.android.screen.comments.model

import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.comment.*
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.CommentsViewModel
import kotlinx.coroutines.CoroutineScope

open class ContentCommentAdapter(
    lifecycleScope: CoroutineScope,
    viewModel: CommentsViewModel,
    private val navigation: CommentViewControllerNavigator,
    private val commentContentFactory: CommentBodyController.CommentContent.Factory,
    private val blockContentFactory: BlockViewController.BlockContent.Factory
) : BaseCommentAdapter<RecyclerView.ViewHolder>(lifecycleScope, viewModel) {

    companion object : Analytics(LogAnalytic())

    override fun getItemViewType(position: Int): Int = when (peek(position)) {
        is CommentModelNode -> super.getItemViewType(position)
        is CommentModelBlank -> 1
        else -> super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        1 -> BlockViewHolder.Factory().create(parent.context, parent)
        else -> CommentViewHolder.Factory().build(parent.context, parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = getItem(position)
        if (model == null) {
            val event = analyticEvent("CommentAdapter", "Comment is null at position $position")
            return capture(event)
        }

        when (model) {
            is CommentModelNode -> {
                onBindViewHolderComment(holder as CommentViewHolder, position, model)
            }
            is CommentModelBlank -> {
                onBindViewHolderBlock(holder as BlockViewHolder, position, model)
            }
        }
    }

    protected open fun onBindViewHolderComment(holder: CommentViewHolder, position: Int, model: CommentModelNode) {
        val commentViewController = CommentViewController(holder).default(model.comment)
        val commentBodyController = CommentBodyController(holder)
        val commentBottomPanelController = CommentBottomPanelController(holder)

        commentViewController.setLevel(model.level).setCommentAvatar(holder, model)
        commentBodyController.setContent(commentContentFactory.build(model.comment.message))
        with(commentBottomPanelController) {
            showExpanded()
            setBookmarkAction {
                Toast.makeText(holder.context, R.string.not_implemented, Toast.LENGTH_LONG).show()
            }
            setReplyAction {
                Toast.makeText(holder.context, R.string.not_implemented, Toast.LENGTH_LONG).show()
            }
            setShareAction {
                Toast.makeText(holder.context, R.string.not_implemented, Toast.LENGTH_LONG).show()
            }
            setOverflowAction {
                navigation.toDetailsScreen(model.comment.commentId)
            }
            setVoteUpAction {
                Toast.makeText(holder.context, R.string.not_implemented, Toast.LENGTH_LONG).show()
            }
            setVoteDownAction {
                Toast.makeText(holder.context, R.string.not_implemented, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onBindViewHolderBlock(holder: BlockViewHolder, position: Int, model: CommentModelBlank) {
        val blockContent = blockContentFactory.build(model.count, model.comment.commentId)
        BlockViewController(holder).setLevel(model.level).setContent(blockContent)
    }
}
