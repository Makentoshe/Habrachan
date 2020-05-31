package com.makentoshe.habrachan.view.comments.controller

import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.model.comments.CommentPopupFactory
import com.makentoshe.habrachan.model.comments.tree.Tree

class NativeCommentBehaviorController(
    private val root: View,
    private val commentPopupFactory: CommentPopupFactory,
    private val commentsTree: Tree<Comment>
) {

    fun setCommentTouchBehavior(comment: Comment) {
        val listener = CommentGestureListener {
            val window = commentPopupFactory.build(root, comment, commentsTree)
            window.showAtLocation(root, Gravity.NO_GRAVITY, it.rawX.toInt(), it.rawY.toInt())
        }
        val detector = GestureDetector(root.context, listener)
        root.setOnTouchListener { _, event -> detector.onTouchEvent(event) }
        // add for ripple effect
        root.setOnLongClickListener { true }
    }

    private class CommentGestureListener(
        private val onLongClick: (MotionEvent) -> Unit
    ) : GestureDetector.OnGestureListener {
        override fun onShowPress(e: MotionEvent?) = Unit
        override fun onSingleTapUp(e: MotionEvent?) = false
        override fun onDown(e: MotionEvent?) = false
        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float) = false
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float) = false
        override fun onLongPress(e: MotionEvent) = onLongClick.invoke(e)
    }

    class Factory(private val commentPopupFactory: CommentPopupFactory, private val commentsTree: Tree<Comment>) {
        fun build(root: View) = NativeCommentBehaviorController(root, commentPopupFactory, commentsTree)
    }
}

