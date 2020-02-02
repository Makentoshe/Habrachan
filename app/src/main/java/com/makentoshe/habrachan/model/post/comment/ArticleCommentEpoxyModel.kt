package com.makentoshe.habrachan.model.post.comment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.comment.Comment

@EpoxyModelClass(layout = R.layout.comments_fragment_comment)
abstract class ArticleCommentEpoxyModel : EpoxyModelWithHolder<ArticleCommentEpoxyModel.ViewHolder>() {

    @EpoxyAttribute
    var message = ""

    @EpoxyAttribute
    var author = ""

    @EpoxyAttribute
    var score = 0

    @EpoxyAttribute
    var timePublished = ""

    var level: Int = 0

    var spannedFactory: SpannedFactory? = null
    var gestureDetectorFactory: OnCommentGestureDetectorFactory? = null
    var comment: Comment? = null

    override fun bind(holder: ViewHolder) {
        holder.messageView?.text = spannedFactory?.build(message)
        holder.authorView?.text = author
        holder.timePublishedView?.text = timePublished
        setCommentLevel(holder)
        setScore(holder)
        setOnClickListener(holder)
    }

    private fun setOnClickListener(holder: ViewHolder) {
        val gestureDetector = gestureDetectorFactory?.build(holder.rootView ?: return, comment ?: return)
        holder.rootView?.setOnTouchListener { _, event ->
            gestureDetector?.onTouchEvent(event) ?: return@setOnTouchListener false
        }
        // add for ripple effect
        holder.rootView?.setOnLongClickListener { true }
    }

    private fun setScore(holder: ViewHolder) {
        holder.scoreView?.text = when {
            score > 0 -> {
                holder.scoreView?.setTextColor(Color.GREEN)
                "+$score"
            }
            score < 0 -> {
                holder.scoreView?.setTextColor(Color.RED)
                score.toString()
            }
            else -> {
                holder.scoreView?.setTextColor(Color.DKGRAY)
                score.toString()
            }
        }
    }

    private fun setCommentLevel(viewHolder: ViewHolder) {
        viewHolder.verticalView?.removeAllViews()
        val count = if (level < 10) level else 10
        repeat(count) {
            LayoutInflater.from(viewHolder.rootView?.context).inflate(
                R.layout.comments_fragment_comment_vertical, viewHolder.verticalView, true
            )
        }
    }

    class ViewHolder : EpoxyHolder() {
        var messageView: TextView? = null
        var authorView: TextView? = null
        var timePublishedView: TextView? = null
        var scoreView: TextView? = null
        var avatarView: ImageView? = null
        var rootView: View? = null
        var verticalView: LinearLayout? = null

        override fun bindView(itemView: View) {
            messageView = itemView.findViewById(R.id.comments_fragment_comment_message)
            authorView = itemView.findViewById(R.id.comments_fragment_comment_author)
            timePublishedView = itemView.findViewById(R.id.comments_fragment_comment_timepublished)
            scoreView = itemView.findViewById(R.id.comments_fragment_comment_score)
            avatarView = itemView.findViewById(R.id.comments_fragment_comment_avatar)
            verticalView = itemView.findViewById(R.id.comments_fragment_comment_verticalcontainer)
            rootView = itemView
        }
    }

    class Factory(
        private val spannedFactory: SpannedFactory,
        private val gestureDetectorFactory: OnCommentGestureDetectorFactory
    ) {
        fun build(comment: Comment): ArticleCommentEpoxyModel {
            val model = ArticleCommentEpoxyModel_().id(comment.id)

            model.message = comment.message
            model.level = comment.level
            model.author = comment.author.login
            model.timePublished = comment.timePublished
            model.score = comment.score

            model.gestureDetectorFactory = gestureDetectorFactory
            model.spannedFactory = spannedFactory
            model.comment = comment
            return model
        }
    }
}
