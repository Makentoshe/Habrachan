package com.makentoshe.habrachan.model.comments

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.Toast
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.viewmodel.comments.CommentsFragmentViewModel

class CommentPopupFactory(private val viewModel: CommentsFragmentViewModel) {

    fun build(anchor: View, comment: Comment): PopupWindow {
        val menu = PopupWindow(anchor.context)
        menu.isOutsideTouchable = true
        menu.contentView =
            LayoutInflater.from(anchor.context).inflate(R.layout.comment_item_popup, null, false)
        menu.contentView.findViewById<View>(R.id.comment_item_popup_reply).setOnClickListener {
            onReplyClick(it.context, comment)
            menu.dismiss()
        }
        menu.contentView.findViewById<View>(R.id.comment_item_popup_voteup).setOnClickListener {
            onVoteUp(comment)
            menu.dismiss()
        }
        menu.contentView.findViewById<View>(R.id.comment_item_popup_votedown).setOnClickListener {
            onVoteDown(comment)
            menu.dismiss()
        }
        menu.contentView.findViewById<View>(R.id.comment_item_popup_inspect).setOnClickListener {
            onInspect(it.context, comment)
            menu.dismiss()
        }
        menu.animationStyle = R.style.Default_Popup_Style
        // elevation="8dp"
        menu.elevation = (8 * Resources.getSystem().displayMetrics.density)
        // removes black border
        menu.setBackgroundDrawable(null)
        return menu
    }

    private fun onReplyClick(context: Context, comment: Comment) {
        Toast.makeText(context, "Reply is not implemented", Toast.LENGTH_LONG).show()
    }

    private fun onVoteUp(comment: Comment) {
        viewModel.voteUpCommentObserver.onNext(comment.id)
    }

    private fun onVoteDown(comment: Comment) {
        viewModel.voteDownCommentObserver.onNext(comment.id)
    }

    private fun onInspect(context: Context, comment: Comment) {
        Toast.makeText(context, "Inspect user: ${comment.author.login} not implemented", Toast.LENGTH_LONG).show()
    }
}
