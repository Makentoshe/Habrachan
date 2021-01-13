package com.makentoshe.habrachan.application.android.screen.comments.model

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.BuildConfig
import com.makentoshe.habrachan.application.android.screen.comments.navigation.ArticleCommentsNavigation
import com.makentoshe.habrachan.application.android.screen.comments.view.BlockViewHolder
import com.makentoshe.habrachan.application.android.screen.comments.view.CommentViewHolder
import com.makentoshe.habrachan.entity.Comment

class CommentAdapter(
    private val navigation: ArticleCommentsNavigation
) : PagingDataAdapter<CommentAdapterModel, RecyclerView.ViewHolder>(CommentDiffUtilItemCallback()) {

    companion object {
        inline fun capture(level: Int, message: () -> String) {
            if (BuildConfig.DEBUG) return
            Log.println(level, "CommentAdapter", message())
        }
    }

    override fun getItemViewType(position: Int): Int = when (peek(position)) {
        is CommentAdapterModel.Comment -> super.getItemViewType(position)
        is CommentAdapterModel.Block -> 1
        null -> super.getItemViewType(position)
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
            is CommentAdapterModel.Comment -> {
                onBindViewHolderComment(holder as CommentViewHolder, position, model.comment)
            }
            is CommentAdapterModel.Block -> {
                onBindViewHolderBlock(holder as BlockViewHolder, position, model)
            }
        }
    }

    private fun onBindViewHolderComment(holder: CommentViewHolder, position: Int, comment: Comment) {
        CommentViewController(holder).setLevel(comment.level).render(comment).setVoteListener({
            Toast.makeText(holder.context, R.string.not_implemented, Toast.LENGTH_LONG).show()
        }, {
            Toast.makeText(holder.context, R.string.not_implemented, Toast.LENGTH_LONG).show()
        }).setVoteScore(comment.score)
    }

    private fun onBindViewHolderBlock(holder: BlockViewHolder, position: Int, model: CommentAdapterModel.Block) {
        BlockViewController(holder).setLevel(model.level).setBody(model.count, model.parent) {
            navigation.toDiscussionCommentsFragment(it)
        }
    }
}
