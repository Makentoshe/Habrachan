package com.makentoshe.habrachan.model.post.comment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupMenu
import android.widget.PopupWindow
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.comment.Comment

class OnCommentClickListenerFactory(private val commentPopupMenuFactory: CommentPopupMenu.Factory) {
    fun buildClickListener(comment: Comment): View.OnClickListener {
        return View.OnClickListener {
            println(comment.author)
        }
    }

    fun buildLongClickListener(comment: Comment): View.OnLongClickListener {
        return View.OnLongClickListener {
            commentPopupMenuFactory.build(comment).show(it.context, it)
            return@OnLongClickListener true
        }
    }
}

class CommentPopupMenu(private val comment: Comment) {

    fun show(context: Context, anchor: View) {
        val menu = PopupWindow(context)
        menu.isOutsideTouchable = true
        menu.contentView = LayoutInflater.from(context).inflate(R.layout.comments_fragment_comment_popup, null, false)
        menu.contentView.findViewById<View>(R.id.comments_fragment_comment_popup_reply).setOnClickListener {
            println("Start reply")
        }
        menu.contentView.findViewById<View>(R.id.comments_fragment_comment_popup_voteup).setOnClickListener {
            println("Vote up")
        }
        menu.contentView.findViewById<View>(R.id.comments_fragment_comment_popup_votedown).setOnClickListener {
            println("Vote down")
        }
        menu.showAsDropDown(anchor)
    }

    class Factory {
        fun build(comment: Comment): CommentPopupMenu {
            return CommentPopupMenu(comment)
        }
    }
}