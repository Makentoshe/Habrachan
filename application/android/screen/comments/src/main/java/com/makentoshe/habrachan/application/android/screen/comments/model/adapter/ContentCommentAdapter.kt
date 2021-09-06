package com.makentoshe.habrachan.application.android.screen.comments.model.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.comment.BlockViewHolder
import com.makentoshe.habrachan.application.android.common.comment.CommentViewHolder
import com.makentoshe.habrachan.application.android.common.comment.controller.block.BlockViewController
import com.makentoshe.habrachan.application.android.common.comment.controller.block.body.content.ContentBodyBlock
import com.makentoshe.habrachan.application.android.common.comment.model.forest.CommentModelBlank
import com.makentoshe.habrachan.application.android.common.comment.model.forest.CommentModelNode
import com.makentoshe.habrachan.application.android.navigation.navigator.UserScreenNavigator
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.controller.CommentAdapterController
import javax.inject.Inject

open class ContentCommentAdapter @Inject constructor(
    private val commentAdapterController: CommentAdapterController,
    private val contentBodyBlockFactory: ContentBodyBlock.Factory,
    private val userScreenNavigator: UserScreenNavigator? = null
) : BaseCommentAdapter<RecyclerView.ViewHolder>() {

    companion object : Analytics(LogAnalytic())

    override fun getItemViewType(position: Int): Int = when (peek(position)) {
        is CommentModelNode -> super.getItemViewType(position)
        is CommentModelBlank -> 1
        else -> throw IllegalStateException("Should not be happen")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        1 -> BlockViewHolder.Factory().create(parent.context, parent)
        else -> CommentViewHolder.Factory().build(parent.context, parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = getItem(position) ?: return capture(analyticEvent("Comment is null at position $position"))

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
        commentAdapterController.onBindViewHolderComment(holder, model)

        holder.setOnClickListenerForHeader {
            userScreenNavigator?.toUserScreen(model.comment.author.login)
        }
    }

    private fun onBindViewHolderBlock(holder: BlockViewHolder, position: Int, model: CommentModelBlank) {
        val blockContent = contentBodyBlockFactory.build(model.count, model.comment.commentId)
        with(BlockViewController(holder).body) {
            level.setLevel(model.level)
            content.setContent(blockContent)
        }
    }
}
