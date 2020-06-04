package com.makentoshe.habrachan.view.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.common.entity.comment.Comment
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

    private val arguments by inject<CommentsReplyScreenArguments>()
    private val commentsReplyFragmentUi by inject<CommentsReplyFragmentUi>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return commentsReplyFragmentUi.inflateView(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // disable touch events for background fragments
        view.setOnClickListener { }

        val original = arguments.comments.first()
        commentsReplyFragmentUi.toolbar.setTitle(original.author.login)
    }

    class Factory {
        fun build(comments: List<Comment>) = CommentsReplyFragment().apply {
            val arguments = CommentsReplyScreenArguments(this)
            arguments.comments = comments
        }
    }
}
