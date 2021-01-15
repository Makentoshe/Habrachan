package com.makentoshe.habrachan.application.android.screen.comments.model

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.BuildConfig
import com.makentoshe.habrachan.application.android.screen.comments.navigation.ArticleCommentsNavigation
import com.makentoshe.habrachan.application.android.screen.comments.view.BlockViewHolder
import com.makentoshe.habrachan.application.android.screen.comments.view.CommentViewHolder

class CommentAdapter(
    private val navigation: ArticleCommentsNavigation
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
        CommentViewController(holder).setLevel(model.level).render(model.comment).setVoteListener({
            Toast.makeText(holder.context, R.string.not_implemented, Toast.LENGTH_LONG).show()
        }, {
            Toast.makeText(holder.context, R.string.not_implemented, Toast.LENGTH_LONG).show()
        }).setVoteScore(model.comment.score)
    }

    private fun onBindViewHolderBlock(holder: BlockViewHolder, position: Int, model: CommentModelBlank) {
        BlockViewController(holder).setLevel(model.level).setBody(model.count, model.comment.id) {
            navigation.toDiscussionCommentsFragment(it)
        }
    }
}
