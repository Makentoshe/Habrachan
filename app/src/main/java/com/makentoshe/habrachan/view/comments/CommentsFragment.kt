package com.makentoshe.habrachan.view.comments

import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.util.isEmpty
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.common.network.response.GetCommentsResponse
import com.makentoshe.habrachan.common.network.response.VoteCommentResponse
import com.makentoshe.habrachan.common.ui.SnackbarErrorController
import com.makentoshe.habrachan.model.comments.CommentsEpoxyController
import com.makentoshe.habrachan.navigation.comments.CommentsNavigator
import com.makentoshe.habrachan.ui.comments.CommentsFragmentUi
import com.makentoshe.habrachan.viewmodel.comments.CommentsFragmentViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import toothpick.ktp.delegate.inject

class CommentsFragment : Fragment() {

    val arguments = Arguments(this)

    private val navigator by inject<CommentsNavigator>()
    private val disposables by inject<CompositeDisposable>()
    private val epoxyController by inject<CommentsEpoxyController>()
    private val commentsViewModel by inject<CommentsFragmentViewModel>()

    private lateinit var appbar: AppBarLayout
    private lateinit var toolbar: Toolbar
    private lateinit var retryButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageView: TextView
    private lateinit var firstCommentButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return CommentsFragmentUi(container).inflateView(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        appbar = view.findViewById(R.id.comments_fragment_appbar)
        toolbar = view.findViewById(R.id.comments_fragment_toolbar)
        retryButton = view.findViewById(R.id.comments_fragment_retrybutton)
        progressBar = view.findViewById(R.id.comments_fragment_progressbar)
        recyclerView = view.findViewById(R.id.comments_fragment_recyclerview)
        messageView = view.findViewById(R.id.comments_fragment_messageview)
        firstCommentButton = view.findViewById(R.id.comments_fragment_firstbutton)

        // disable touch events for background fragments
        view.setOnClickListener { }

        commentsViewModel.getCommentsObservable.observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onGetCommentsResponse).let(disposables::add)

        commentsViewModel.voteUpCommentObservable.observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onVoteCommentsResponse).let(disposables::add)

        commentsViewModel.voteDownCommentObservable.observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onVoteCommentsResponse).let(disposables::add)

        toolbar.setNavigationOnClickListener { navigator.back() }
        arguments.article?.title?.let(toolbar::setTitle)
        toolbar.setSubtitle(R.string.comments_fragment_subtitle)
        retryButton.setOnClickListener { onRetryButtonClicked() }
        appbar.setExpanded(true)

        if (savedInstanceState == null) {
            commentsViewModel.getCommentsObserver.onNext(arguments.articleId)
        }
    }

    private fun onGetCommentsResponse(response: GetCommentsResponse) = when (response) {
        is GetCommentsResponse.Success -> onGetCommentsResponseSuccess(response)
        is GetCommentsResponse.Error -> onGetCommentsResponseError(response)
    }

    private fun onGetCommentsResponseSuccess(response: GetCommentsResponse.Success) {
        val commentsArray = commentsViewModel.toSparseArray(response.data, arguments.articleId)
        if (commentsArray.isEmpty()) displayCommentsThumbnail() else displayComments(commentsArray)
    }

    private fun displayComments(comments: SparseArray<ArrayList<Comment>>) {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        epoxyController.setComments(comments)
        recyclerView.adapter = epoxyController.adapter
        epoxyController.requestModelBuild()
        recyclerView.visibility = View.VISIBLE
    }

    private fun displayCommentsThumbnail() {
        messageView.setText(R.string.comments_message_no_comments)
        messageView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        firstCommentButton.visibility = View.VISIBLE
    }

    private fun onGetCommentsResponseError(response: GetCommentsResponse.Error) {
        retryButton.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        messageView.visibility = View.VISIBLE
        messageView.text = response.message.plus("\n").plus(response.additional.joinToString("\n"))
    }

    private fun onRetryButtonClicked() {
        retryButton.visibility = View.GONE
        messageView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        commentsViewModel.getCommentsObserver.onNext(arguments.articleId)
    }

    private fun onVoteCommentsResponse(response: VoteCommentResponse) = when (response) {
        is VoteCommentResponse.Success -> onVoteCommentsResponseSuccess(response)
        is VoteCommentResponse.Error -> onVoteCommentsResponseError(response)
    }

    private fun onVoteCommentsResponseSuccess(response: VoteCommentResponse.Success) {
        epoxyController.updateCommentScore(response.request.commentId, response.score)
        epoxyController.requestModelBuild()
    }

    private fun onVoteCommentsResponseError(response: VoteCommentResponse.Error) {
        val message = when (response.code) {
            498 -> requireContext().getString(R.string.network_check_error)
            401 -> requireContext().getString(R.string.should_be_logged_in_for_action)
            400 -> requireContext().getString(R.string.repeated_voting_is_prohibited)
            else -> response.message.plus(" ").plus(response.additional.joinToString(". "))
        }
        SnackbarErrorController.from(view ?: return).displayMessage(message)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    class Factory {

        fun build(articleId: Int, article: Article? = null) = CommentsFragment().apply {
            arguments.article = article
            arguments.articleId = articleId
        }
    }

    class Arguments(private val commentsFragment: CommentsFragment) {

        init {
            val fragment = commentsFragment as Fragment
            if (fragment.arguments == null) {
                fragment.arguments = Bundle()
            }
        }

        private val fragmentArguments: Bundle
            get() = commentsFragment.requireArguments()

        var articleId: Int
            set(value) = fragmentArguments.putInt(ID, value)
            get() = fragmentArguments.getInt(ID, -1)

        var article: Article?
            set(value) = fragmentArguments.putString(ARTICLE, value?.toJson())
            get() = fragmentArguments.getString(ARTICLE)?.let(Article.Companion::fromJson)

        companion object {
            private const val ID = "Id"
            private const val ARTICLE = "Article"
        }
    }

}
