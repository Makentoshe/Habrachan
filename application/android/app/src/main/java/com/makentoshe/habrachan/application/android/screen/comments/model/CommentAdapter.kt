package com.makentoshe.habrachan.application.android.screen.comments.model

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.BuildConfig
import com.makentoshe.habrachan.application.android.dp2px
import com.makentoshe.habrachan.application.android.screen.comments.navigation.CommentsNavigation
import com.makentoshe.habrachan.application.android.screen.comments.view.BlockViewHolder
import com.makentoshe.habrachan.application.android.screen.comments.view.CommentViewHolder
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.CommentsViewModel
import com.makentoshe.habrachan.application.android.toRoundedDrawable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CommentAdapter(
    private val navigation: CommentsNavigation,
    private val lifecycleScope: CoroutineScope,
    private val viewModel: CommentsViewModel
) : PagingDataAdapter<CommentModelElement, RecyclerView.ViewHolder>(CommentDiffUtilItemCallback()) {

    companion object {
        inline fun capture(level: Int, message: () -> String) {
            if (BuildConfig.DEBUG) return
            Log.println(level, "CommentAdapter", message())
        }
    }

    override fun getItemViewType(position: Int): Int = when (peek(position)) {
        is CommentModelNode -> super.getItemViewType(position)
        is CommentModelBlank -> 1
        else -> super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            1 -> BlockViewHolder(inflater.inflate(R.layout.layout_comment_block, parent, false))
            else -> CommentViewHolder(inflater.inflate(R.layout.layout_comment_item, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = getItem(position) ?: return capture(Log.ERROR) {
            "Comment is null at position $position"
        }

        when (model) {
            is CommentModelNode-> {
                onBindViewHolderComment(holder as CommentViewHolder, position, model)
            }
            is CommentModelBlank -> {
                onBindViewHolderBlock(holder as BlockViewHolder, position, model)
            }
        }
    }

    private fun onBindViewHolderComment(holder: CommentViewHolder, position: Int, model: CommentModelNode) {
        val controller = CommentViewController(holder, navigation).install(model.comment).setVoteListener({
            Toast.makeText(holder.context, R.string.not_implemented, Toast.LENGTH_LONG).show()
        }, {
            Toast.makeText(holder.context, R.string.not_implemented, Toast.LENGTH_LONG).show()
        }).setStubAvatar().setLevel(model.level)

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.requestAvatar(model.comment.avatar).collectLatest { result ->
                result.onFailure { controller.setStubAvatar() }.onSuccess {
                    val resources = holder.context.resources
                    val radius = holder.context.dp2px(R.dimen.radiusS)
                    controller.setAvatar(it.bytes.toRoundedDrawable(resources, radius))
                }
            }
        }
    }

    private fun onBindViewHolderBlock(holder: BlockViewHolder, position: Int, model: CommentModelBlank) {
        BlockViewController(holder).setLevel(model.level).setBody(model.count, model.comment.id) {
            navigation.toDiscussionCommentsFragment(it)
        }
    }
}
