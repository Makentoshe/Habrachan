package com.makentoshe.habrachan.model.comments

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.model.user.UserAccount
import com.makentoshe.habrachan.model.user.UserScreen
import com.makentoshe.habrachan.view.commentinput.CommentInputScreen
import com.makentoshe.habrachan.viewmodel.comments.CommentsFragmentViewModel

class CommentPopupFactory(private val viewModel: CommentsFragmentViewModel, private val router: Router) {

    fun build(anchor: View, comment: Comment): PopupWindow {
        val menu = PopupWindow(anchor.context)
        menu.isOutsideTouchable = true
        menu.contentView =
            LayoutInflater.from(anchor.context).inflate(R.layout.comment_item_popup, null, false)
        menu.contentView.findViewById<View>(R.id.comment_item_popup_reply).setOnClickListener {
            onReplyClick(comment)
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
            onInspect(comment)
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
        router.navigateTo(CommentInputScreen(comment))
    }

    private fun onVoteUp(comment: Comment) {
        viewModel.voteUpCommentObserver.onNext(comment.id)
    }

    private fun onVoteDown(comment: Comment) {
        viewModel.voteDownCommentObserver.onNext(comment.id)
    }

    private fun onInspect(comment: Comment) {
        router.navigateTo(UserScreen(UserAccount.User(comment.author.login)))
    }
}
