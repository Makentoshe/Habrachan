package com.makentoshe.habrachan.model.comments

import android.os.Build
import android.text.Html
import android.view.GestureDetector
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.common.ui.TextScoreController
import kotlin.math.min

class CommentEpoxyModelsController(
    private val commentPopupFactory: CommentPopupFactory,
    private val avatarControllerFactory: CommentAvatarController.Factory
) {

    fun setMessage(message: String, holder: CommentEpoxyModel.ViewHolder) {
        holder.messageView?.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY, null, null)
        } else {
            Html.fromHtml(message, null, null)
        }
    }

    fun setScore(score: Int, holder: CommentEpoxyModel.ViewHolder) {
        val controller = TextScoreController(holder.scoreView ?: return)
        controller.setScoreDark(holder.scoreView?.context ?: return, score)
    }

    fun setCommentLevel(level: Int, holder: CommentEpoxyModel.ViewHolder) {
        holder.verticalView?.removeAllViews()
        repeat(min(level, 10)) {
            LayoutInflater.from(holder.rootView?.context).inflate(
                R.layout.comments_fragment_comment_vertical, holder.verticalView, true
            )
        }
    }

    fun setOnClickListener(comment: Comment, holder: CommentEpoxyModel.ViewHolder) {
        val view = holder.rootView ?: return
        val listener = buildGestureLongClickListener {
            val window = commentPopupFactory.build(view, comment)
            window.showAtLocation(view, Gravity.NO_GRAVITY, it.rawX.toInt(), it.rawY.toInt())
        }
        val detector = GestureDetector(view.context, listener)
        view.setOnTouchListener { _, event ->
            detector.onTouchEvent(event)
        }
        // add for ripple effect
        view.setOnLongClickListener { true }
    }

    private fun buildGestureLongClickListener(onLongClick: (MotionEvent) -> Unit) = object :
        GestureDetector.OnGestureListener {
        override fun onShowPress(e: MotionEvent?) = Unit
        override fun onSingleTapUp(e: MotionEvent?) = false
        override fun onDown(e: MotionEvent?) = false
        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float) = false
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float) = false
        override fun onLongPress(e: MotionEvent) = onLongClick.invoke(e)
    }

    fun setAvatar(avatarUrl: String, holder: CommentEpoxyModel.ViewHolder) {
        avatarControllerFactory.build(avatarUrl).toAvatarView(holder)
    }

}