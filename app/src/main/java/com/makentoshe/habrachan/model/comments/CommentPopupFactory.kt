package com.makentoshe.habrachan.model.comments

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.model.comments.tree.Tree
import com.makentoshe.habrachan.model.user.UserAccount
import com.makentoshe.habrachan.navigation.user.UserScreen
import com.makentoshe.habrachan.viewmodel.comments.CommentsFragmentViewModel
import ru.terrakok.cicerone.Router

class CommentPopupFactory(
    private val viewModel: CommentsFragmentViewModel,
    private val router: Router
) {

    fun build(anchor: View, comment: Comment, commentsTree: Tree<Comment>): PopupWindow {
        val menu = PopupWindow(anchor.context)
        menu.isOutsideTouchable = true
        menu.contentView =
            LayoutInflater.from(anchor.context).inflate(R.layout.comment_item_popup, null, false)
        menu.contentView.findViewById<View>(R.id.comment_item_popup_reply).setOnClickListener {
            onReplyClick(comment, commentsTree)
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

    private fun onReplyClick(comment: Comment, commentsTree: Tree<Comment>) {
        val node = commentsTree.findNode { it == comment }
        val path = commentsTree.pathToRoot(node!!)
        println(path)
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
