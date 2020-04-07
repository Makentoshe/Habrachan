package com.makentoshe.habrachan.model.comments

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.comment.Comment

@EpoxyModelClass(layout = R.layout.comments_fragment_comment)
abstract class CommentEpoxyModel : EpoxyModelWithHolder<CommentEpoxyModel.ViewHolder>() {

    @EpoxyAttribute
    var message = ""

    @EpoxyAttribute
    var author = ""

    @EpoxyAttribute
    var score = 0

    @EpoxyAttribute
    var timePublished = ""

    @EpoxyAttribute
    var avatarUrl: String = ""

    @EpoxyAttribute
    var level: Int = 0

    @EpoxyAttribute
    var commentId: Int = 0

    lateinit var comment: Comment

    lateinit var modelsController: CommentEpoxyModelsController

    override fun bind(holder: ViewHolder) {
        holder.authorView?.text = author
        holder.timePublishedView?.text = timePublished

        modelsController.setMessage(message, holder)
        modelsController.setScore(score, holder)
        modelsController.setCommentLevel(level, holder)
        modelsController.setOnClickListener(comment, holder)
        modelsController.setAvatar(avatarUrl, holder)
    }

    class ViewHolder : EpoxyHolder() {
        var messageView: TextView? = null
        var authorView: TextView? = null
        var timePublishedView: TextView? = null
        var scoreView: TextView? = null
        var avatarView: ImageView? = null
        var rootView: View? = null
        var verticalView: LinearLayout? = null
        var progressView: ProgressBar? = null

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

    class Factory(private val commentEpoxyModelsController: CommentEpoxyModelsController) {
        fun build(comment: Comment): CommentEpoxyModel {
            val model = CommentEpoxyModel_().id(comment.id)

            model.message = comment.message
            model.level = comment.level
            model.author = comment.author.login
            model.timePublished = comment.timePublished
            model.score = comment.score
            model.commentId = comment.id
            model.avatarUrl = comment.avatar

            model.modelsController = commentEpoxyModelsController
            model.comment = comment

            return model
        }
    }
}
