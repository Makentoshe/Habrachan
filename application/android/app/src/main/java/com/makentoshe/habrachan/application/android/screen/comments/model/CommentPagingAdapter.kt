package com.makentoshe.habrachan.application.android.screen.comments.model

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.paging.PagingDataAdapter
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.BuildConfig
import com.makentoshe.habrachan.application.android.screen.comments.RepliesCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.navigation.ArticleCommentsNavigation
import com.makentoshe.habrachan.application.android.screen.comments.view.CommentViewHolder

abstract class CommentPagingAdapter(
    protected val fragmentManager: FragmentManager,
    protected val articleId: Int,
    protected val navigation: ArticleCommentsNavigation
) : PagingDataAdapter<CommentModel, CommentViewHolder>(CommentDiffUtilItemCallback()) {

    companion object {
        inline fun capture(level: Int, message: () -> String) {
            if (BuildConfig.DEBUG) return
            Log.println(level, "CommentAdapter", message())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CommentViewHolder(inflater.inflate(R.layout.layout_comment_item, parent, false))
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val model = getItem(position) ?: return capture(Log.ERROR) {
            "Comment is null at position $position"
        }

        val controller = CommentViewController(holder).render(model.comment)
        // TODO implement comments voting
        controller.setVoteListener({
            Toast.makeText(holder.context, R.string.not_implemented, Toast.LENGTH_LONG).show()
        }, {
            Toast.makeText(holder.context, R.string.not_implemented, Toast.LENGTH_LONG).show()
        })

        controller.setUserClickListener {
            navigation.toUserScreen(holder.context)
        }

        onBindViewHolder(controller, holder, model)
    }

    abstract fun onBindViewHolder(
        controller: CommentViewController, holder: CommentViewHolder, model: CommentModel
    )
}

class ReplyCommentPagingAdapter(
    fragmentManager: FragmentManager, articleId: Int, navigation: ArticleCommentsNavigation
) : CommentPagingAdapter(fragmentManager, articleId, navigation) {

    override fun onBindViewHolder(
        controller: CommentViewController, holder: CommentViewHolder, model: CommentModel
    ) = controller.setReplies(model.childs.count()) {
        if (model.childs.isEmpty()) {
            navigation.toCommentReply(holder.context)
        } else {
            val fragment = RepliesCommentsFragment.build(articleId, model.comment.id)
            fragment.show(fragmentManager, model.comment.id.toString())
        }
    }
}

class TitleCommentPagingAdapter(
    fragmentManager: FragmentManager, articleId: Int, navigation: ArticleCommentsNavigation
) : CommentPagingAdapter(fragmentManager, articleId, navigation) {

    override fun onBindViewHolder(
        controller: CommentViewController, holder: CommentViewHolder, model: CommentModel
    ) = controller.setReplies(0) { navigation.toCommentReply(holder.context) }
}
