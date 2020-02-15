package com.makentoshe.habrachan.model.post.comment

import android.graphics.Color
import android.view.LayoutInflater
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
import io.reactivex.disposables.CompositeDisposable

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

    @EpoxyAttribute
    var avatarUrl: String = ""

    var level: Int = 0

    var spannedFactory: SpannedFactory? = null
    var gestureDetectorBuilder: OnCommentGestureDetectorBuilder? = null
    var avatarController: ArticleCommentAvatarController? = null

    override fun bind(holder: ViewHolder) {
        holder.messageView?.text = spannedFactory?.build(message)
        holder.authorView?.text = author
        holder.timePublishedView?.text = timePublished
        avatarController?.toAvatarView(holder)
        setCommentLevel(holder)
        setScore(holder)
        setOnClickListener(holder)
    }

    private fun setOnClickListener(holder: ViewHolder) {
        gestureDetectorBuilder!!.view = holder.rootView
        val gestureDetector = gestureDetectorBuilder!!.build()
        holder.rootView?.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
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

    class Factory(
        private val spannedFactory: SpannedFactory,
        private val gestureDetectorBuilder: OnCommentGestureDetectorBuilder,
        private val avatarControllerFactory: ArticleCommentAvatarController.Factory
    ) {
        fun build(comment: Comment): ArticleCommentEpoxyModel {
            val model = ArticleCommentEpoxyModel_().id(comment.id)

            model.message = comment.message
            model.level = comment.level
            model.author = comment.author.login
            model.timePublished = comment.timePublished
            model.score = comment.score

            model.gestureDetectorBuilder = gestureDetectorBuilder.also { it.comment = comment }
            model.spannedFactory = spannedFactory
            model.avatarController = avatarControllerFactory.build(comment.avatar)
            return model
        }
    }
}
