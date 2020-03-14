package com.makentoshe.habrachan.model.article.comment

import android.content.Context
import android.content.res.Resources
import android.view.*
import android.widget.PopupWindow
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.comment.Comment

class OnCommentGestureDetectorBuilder {

    var comment: Comment? = null
    var view: View? = null

    private fun build(view: View, comment: Comment): GestureDetector {
        val listener = object : GestureDetector.OnGestureListener {
            override fun onShowPress(e: MotionEvent?) = Unit
            override fun onSingleTapUp(e: MotionEvent?) = false
            override fun onDown(e: MotionEvent?) = false
            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float) = false
            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float) = false
            override fun onLongPress(e: MotionEvent) {
                val menu = createPopup(view.context, comment)
                menu.showAtLocation(view, Gravity.NO_GRAVITY, e.rawX.toInt(), e.rawY.toInt())
            }
        }
        return GestureDetector(view.context, listener)
    }

    fun build(): GestureDetector{
        return build(view!!, comment!!)
    }

    private fun createPopup(context: Context, comment: Comment): PopupWindow {
        val menu = PopupWindow(context)
        menu.isOutsideTouchable = true
        menu.contentView = LayoutInflater.from(context).inflate(R.layout.comments_fragment_comment_popup, null, false)
        menu.contentView.findViewById<View>(R.id.comments_fragment_comment_popup_reply).setOnClickListener {
            onReplyClick(comment)
            menu.dismiss()
        }
        menu.contentView.findViewById<View>(R.id.comments_fragment_comment_popup_voteup).setOnClickListener {
            onVoteUp(comment)
            menu.dismiss()
        }
        menu.contentView.findViewById<View>(R.id.comments_fragment_comment_popup_votedown).setOnClickListener {
            onVoteDown(comment)
            menu.dismiss()
        }
        menu.animationStyle = R.style.Default_Popup_Style
        // elevation="8dp"
        menu.elevation = (8 * Resources.getSystem().displayMetrics.density)
        // removes black border
        menu.setBackgroundDrawable(null)
        return menu
    }

    private fun onReplyClick(comment: Comment) {
        println("Start reply")
    }

    private fun onVoteUp(comment: Comment) {
        println("Vote up")
    }

    private fun onVoteDown(comment: Comment) {
        println("Vote down")
    }
}