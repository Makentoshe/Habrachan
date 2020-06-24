package com.makentoshe.habrachan.view.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.common.ui.softkeyboard.SoftKeyboardController
import com.makentoshe.habrachan.model.comments.CommentsEpoxyController
import com.makentoshe.habrachan.common.model.tree.Tree
import com.makentoshe.habrachan.common.model.tree.TreeNode
import com.makentoshe.habrachan.navigation.comments.CommentsReplyScreenArguments
import com.makentoshe.habrachan.ui.comments.CommentsInputFragmentUi
import com.makentoshe.habrachan.ui.comments.CommentsReplyFragmentUi
import com.makentoshe.habrachan.viewmodel.comments.SendCommentViewModel
import io.reactivex.disposables.CompositeDisposable
import toothpick.ktp.delegate.inject

class CommentsReplyFragment : CommentsInputFragment() {

    override val commentsInputFragmentUi by inject<CommentsInputFragmentUi>()
    override val sendCommentViewModel by inject<SendCommentViewModel>()
    override val disposables by inject<CompositeDisposable>()
    override val softKeyboardController = SoftKeyboardController()

    private val commentsEpoxyController by inject<CommentsEpoxyController>()
    private val arguments by inject<CommentsReplyScreenArguments>()
    private val commentsReplyFragmentUi by inject<CommentsReplyFragmentUi>()

    override val replyToParentId: Int
        get() = arguments.comments.last().id

    override val articleId: Int
        get() = arguments.articleId

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return commentsReplyFragmentUi.inflateView(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // disable touch events for background fragments
        view.setOnClickListener { }

        val original = arguments.comments.last()
        commentsReplyFragmentUi.toolbar.setTitle(original.author.login)

        commentsEpoxyController.setComments(Tree(arguments.comments.map(::TreeNode)))
        commentsReplyFragmentUi.recycler.adapter = commentsEpoxyController.adapter
        commentsEpoxyController.requestModelBuild()
    }

    class Factory {
        fun build(comments: List<Comment>, articleId: Int) = CommentsReplyFragment().apply {
            val arguments = CommentsReplyScreenArguments(this)
            arguments.comments = comments.map { it.copy(level = 0) }
            arguments.articleId = articleId
        }
    }
}