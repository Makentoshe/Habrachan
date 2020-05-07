package com.makentoshe.habrachan.model.comments

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.view.comments.controller.NativeCommentController

@EpoxyModelClass(layout = R.layout.comments_fragment_comment)
abstract class CommentEpoxyModel : EpoxyModelWithHolder<CommentEpoxyModel.ViewHolder>() {

    lateinit var comment: Comment

    lateinit var controller: NativeCommentController

    override fun bind(holder: ViewHolder) {
        holder.authorView.text = comment.author.login
        holder.timePublishedView.text = comment.timePublished

        controller.levelFactory().build(holder.verticalView).setCommentLevel(comment.level)
        controller.messageFactory().build(holder.messageView).setCommentText(comment.message)
        controller.scoreFactory().build(holder.scoreView).setCommentScore(comment.score)
        controller.behaviorFactory().build(holder.rootView).setCommentTouchBehavior(comment)
        controller.avatarFactory().build(holder.avatarView).setCommentAvatar(comment.avatar) {
            holder.avatarView.visibility = View.VISIBLE
            holder.progressView.visibility = View.GONE
        }
    }

    class ViewHolder : EpoxyHolder() {
        lateinit var messageView: TextView
        lateinit var authorView: TextView
        lateinit var timePublishedView: TextView
        lateinit var scoreView: TextView
        lateinit var avatarView: ImageView
        lateinit var rootView: View
        lateinit var verticalView: LinearLayout
        lateinit var progressView: ProgressBar

        override fun bindView(itemView: View) {
            messageView = itemView.findViewById(R.id.comments_fragment_comment_message)
            authorView = itemView.findViewById(R.id.comments_fragment_comment_author)
            timePublishedView = itemView.findViewById(R.id.comments_fragment_comment_timepublished)
            scoreView = itemView.findViewById(R.id.comments_fragment_comment_score)
            avatarView = itemView.findViewById(R.id.comments_fragment_comment_avatar)
            verticalView = itemView.findViewById(R.id.comments_fragment_comment_verticalcontainer)
            progressView = itemView.findViewById(R.id.comments_fragment_comment_progressbar)
            rootView = itemView
        }
    }

    class Factory(private val controller: NativeCommentController) {
        fun build(comment: Comment): CommentEpoxyModel {
            val model = CommentEpoxyModel_().id(comment.id)
            model.controller = controller
            model.comment = comment
            return model
        }
    }
}
