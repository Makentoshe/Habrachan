package com.makentoshe.habrachan.model.comments

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.comment.Comment

class CommentPopupFactory(private val commentActionProvider: CommentActionProvider) {

    fun build(anchor: View, comment: Comment): PopupWindow {
        val inflater = LayoutInflater.from(anchor.context)
        val menu = PopupWindow(anchor.context)
        menu.isOutsideTouchable = true
        menu.contentView = inflater.inflate(R.layout.comment_item_popup, null, false)
        menu.animationStyle = R.style.Default_Popup_Style
        // elevation="8dp"
        menu.elevation = (8 * Resources.getSystem().displayMetrics.density)
        // removes black border
        menu.setBackgroundDrawable(null)

        menu.contentView.findViewById<View>(R.id.comment_item_popup_reply).setOnClickListener {
            commentActionProvider.replyCommentObserver.onNext(comment)
            menu.dismiss()
        }
        menu.contentView.findViewById<View>(R.id.comment_item_popup_voteup).setOnClickListener {
            commentActionProvider.voteUpCommentObserver.onNext(comment)
            menu.dismiss()
        }
        menu.contentView.findViewById<View>(R.id.comment_item_popup_votedown).setOnClickListener {
            commentActionProvider.voteDownCommentObserver.onNext(comment)
            menu.dismiss()
        }
        menu.contentView.findViewById<View>(R.id.comment_item_popup_inspect).setOnClickListener {
            commentActionProvider.inspectUserObserver.onNext(comment)
            menu.dismiss()
        }

        return menu
    }

//    private fun onReplyClick(comment: Comment, commentsTree: Tree<Comment>) {
//        val node = commentsTree.findNode { it == comment }
//        val path = commentsTree.pathToRoot(node!!).reversed()
//        navigation.toReplyScreen(path.map { it.value }, articleId)
//    }
}
