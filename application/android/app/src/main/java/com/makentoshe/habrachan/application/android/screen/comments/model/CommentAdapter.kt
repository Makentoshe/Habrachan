package com.makentoshe.habrachan.application.android.screen.comments.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.comment.BlockViewController
import com.makentoshe.habrachan.application.android.common.comment.BlockViewHolder
import com.makentoshe.habrachan.application.android.common.comment.CommentViewController
import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
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
    private val commentContentFactory: CommentViewController.CommentContent.Factory,
    private val blockContentFactory: BlockViewController.BlockContent.Factory
) : PagingDataAdapter<CommentModelElement, RecyclerView.ViewHolder>(CommentDiffUtilItemCallback()) {

    companion object : Analytics(LogAnalytic())

    override fun getItemViewType(position: Int): Int = when (peek(position)) {
        is CommentModelNode -> super.getItemViewType(position)
        is CommentModelBlank -> 1
        else -> super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            1 -> BlockViewHolder(inflater.inflate(R.layout.layout_comment_block, parent, false))
            else -> CommentViewHolder(inflater.inflate(R.layout.layout_comment_item, parent, false))
        }
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
        val controller = CommentViewController(holder).default(model.comment).setLevel(model.level)
        controller.setContent(commentContentFactory.build(model.comment.message))

//        holder.itemView.setOnClickListener {
//            CommentDetailsDialogFragment.build().show(fragmentManager, "sas")
//        }

        val avatar = model.comment.avatar
        if (avatar == null) controller.setStubAvatar() else lifecycleScope.launch(Dispatchers.IO) {
            viewModel.requestAvatar(avatar).collectLatest { result ->
                result.onFailure { controller.setStubAvatar() }.onSuccess {
                    val resources = holder.context.resources
                    val radius = holder.context.dp2px(R.dimen.radiusS)
                    launch(Dispatchers.Main) { controller.setAvatar(it.bytes.toRoundedDrawable(resources, radius)) }
                }
            }
        }
    }

    private fun onBindViewHolderBlock(holder: BlockViewHolder, position: Int, model: CommentModelBlank) {
        val blockContent = blockContentFactory.build(model.count, model.comment.commentId)
        BlockViewController(holder).setLevel(model.level).setContent(blockContent)
    }
}
