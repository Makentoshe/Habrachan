package com.makentoshe.habrachan.application.android.screen.comments.model

import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.comment.*
import com.makentoshe.habrachan.application.android.dp2px
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.CommentsViewModel
import com.makentoshe.habrachan.application.android.toRoundedDrawable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CommentAdapter(
    private val lifecycleScope: CoroutineScope,
    private val viewModel: CommentsViewModel,
    private val navigation: CommentViewControllerNavigator,
    private val commentContentFactory: CommentBodyController.CommentContent.Factory,
    private val blockContentFactory: BlockViewController.BlockContent.Factory
) : PagingDataAdapter<CommentModelElement, RecyclerView.ViewHolder>(CommentDiffUtilItemCallback()) {

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

    private fun onBindViewHolderComment(holder: CommentViewHolder, position: Int, model: CommentModelNode) {
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

    private fun CommentViewController.setCommentAvatar(holder: CommentViewHolder, model: CommentModelNode) {
        val avatar = model.comment.avatar
        if (avatar == null) setStubAvatar() else lifecycleScope.launch(Dispatchers.IO) {
            viewModel.requestAvatar(avatar).collectLatest { result ->
                result.onFailure { setStubAvatar() }.onSuccess {
                    val resources = holder.context.resources
                    val radius = holder.context.dp2px(R.dimen.radiusS)
                    launch(Dispatchers.Main) { setAvatar(it.bytes.toRoundedDrawable(resources, radius)) }
                }
            }
        }
    }

    private fun onBindViewHolderBlock(holder: BlockViewHolder, position: Int, model: CommentModelBlank) {
        val blockContent = blockContentFactory.build(model.count, model.comment.commentId)
        BlockViewController(holder).setLevel(model.level).setContent(blockContent)
    }
}
