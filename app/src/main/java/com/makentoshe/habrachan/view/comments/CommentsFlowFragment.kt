package com.makentoshe.habrachan.view.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.common.model.Comments
import com.makentoshe.habrachan.common.network.response.GetCommentsResponse
import com.makentoshe.habrachan.common.ui.softkeyboard.SoftKeyboardController
import com.makentoshe.habrachan.navigation.comments.CommentsDisplayFragmentScreen
import com.makentoshe.habrachan.navigation.comments.CommentsFlowFragmentArguments
import com.makentoshe.habrachan.navigation.comments.CommentsFragmentNavigation
import com.makentoshe.habrachan.ui.comments.CommentsFlowFragmentUi
import com.makentoshe.habrachan.ui.comments.CommentsInputFragmentUi
import com.makentoshe.habrachan.viewmodel.comments.GetCommentViewModel
import com.makentoshe.habrachan.viewmodel.comments.SendCommentViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import toothpick.ktp.delegate.inject

class CommentsFlowFragment : CommentsInputFragment() {

    private val arguments = CommentsFlowFragmentArguments(this)

    private val getCommentsViewModel by inject<GetCommentViewModel>()
    override val sendCommentViewModel by inject<SendCommentViewModel>()

    override val commentsInputFragmentUi by inject<CommentsInputFragmentUi>()
    override val disposables by inject<CompositeDisposable>()
    override val softKeyboardController = SoftKeyboardController()

    private val navigation by inject<CommentsFragmentNavigation>()
    private val commentsFlowFragmentUi by inject<CommentsFlowFragmentUi>()

    override val articleId: Int
        get() = arguments.articleId

    private var parentId: Int = 0

    private lateinit var toolbar: Toolbar
    private lateinit var retryButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var messageView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return commentsFlowFragmentUi.inflateView(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        retryButton = view.findViewById(R.id.comments_fragment_retrybutton)
        progressBar = view.findViewById(R.id.comments_fragment_progressbar)
        messageView = view.findViewById(R.id.comments_fragment_messageview)
        toolbar = view.findViewById(R.id.comments_fragment_toolbar)

        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener {
            navigation.back()
        }
        retryButton.setOnClickListener {
            onRetryButtonClicked()
        }

        val comments = arguments.comments
        if (comments != null) {
            onViewCreatedComments(view, savedInstanceState, comments)
        } else {
            onViewCreatedArticle(view, savedInstanceState)
        }

    }

    private fun onViewCreatedComments(view: View, savedInstanceState: Bundle?, comments: List<Comment>) {
        parentId = comments.last().id

        displayComments(comments)
    }

    private fun onViewCreatedArticle(view: View, savedInstanceState: Bundle?) {
        parentId = 0

        toolbar.setSubtitle(R.string.comments_fragment_subtitle)
        arguments.article?.title?.let(toolbar::setTitle)

        // callback for comments request - displays comments
        getCommentsViewModel.getCommentsObservable.observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onGetCommentsResponse).let(disposables::add)

        if (savedInstanceState == null) {
            getCommentsViewModel.getCommentsObserver.onNext(arguments.articleId)
        }
    }

    private fun onGetCommentsResponse(response: GetCommentsResponse) = when (response) {
        is GetCommentsResponse.Success -> onGetCommentsResponseSuccess(response)
        is GetCommentsResponse.Error -> onGetCommentsResponseError(response)
    }

    private fun onGetCommentsResponseSuccess(response: GetCommentsResponse.Success) {
        val commentsTree = Comments.convertCommentsListToCommentsTree(response.data)
        if (commentsTree.isEmpty()) displayCommentsThumbnail() else displayComments(response.data)
    }

    private fun displayComments(comments: List<Comment>) {
        progressBar.visibility = View.GONE

        val containedFragment = childFragmentManager.findFragmentById(R.id.comments_fragment_container)
        if (containedFragment == null) {
            val commentsFragment = CommentsDisplayFragmentScreen(
                arguments.articleId, arguments.commentActionsEnabled, comments
            ).fragment
            childFragmentManager.beginTransaction().add(R.id.comments_fragment_container, commentsFragment).commitNow()
        }
    }

    private fun displayCommentsThumbnail() {
        messageView.setText(R.string.comments_message_no_comments)
        messageView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private fun onGetCommentsResponseError(response: GetCommentsResponse.Error) {
        retryButton.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        messageView.visibility = View.VISIBLE
        messageView.text = response.raw
    }

    private fun onRetryButtonClicked() {
        retryButton.visibility = View.GONE
        messageView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        getCommentsViewModel.getCommentsObserver.onNext(arguments.articleId)
    }
}