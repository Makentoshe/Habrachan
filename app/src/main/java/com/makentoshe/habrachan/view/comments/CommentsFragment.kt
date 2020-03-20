package com.makentoshe.habrachan.view.comments

import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.util.isEmpty
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.database.AvatarDao
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.common.entity.comment.GetCommentsResponse
import com.makentoshe.habrachan.common.entity.comment.VoteCommentResponse
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.model.comments.*
import com.makentoshe.habrachan.ui.article.comments.CommentsFragmentUi
import com.makentoshe.habrachan.viewmodel.comments.CommentsFragmentViewModel
import io.reactivex.disposables.CompositeDisposable
import toothpick.ktp.delegate.inject

class CommentsFragment : Fragment() {

    private val arguments = Arguments(this)

    private val disposables by inject<CompositeDisposable>()
    private val commentsViewModel by inject<CommentsFragmentViewModel>()
    private val navigator by inject<Navigator>()
    private val epoxyController by inject<CommentsEpoxyController>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return CommentsFragmentUi().createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            val request = commentsViewModel.createGetRequest(arguments.articleId)
            commentsViewModel.getCommentsObserver.onNext(request)
        }

        val retrybutton = view.findViewById<View>(R.id.article_comments_retrybutton)
        retrybutton.setOnClickListener(::onRetryButtonClicked)

        val toolbar = view.findViewById<Toolbar>(R.id.article_comments_toolbar)
        toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back, requireContext().theme)
        toolbar.setNavigationOnClickListener { navigator.back() }

        commentsViewModel.getCommentsObservable.subscribe(::onGetCommentsResponse).let(disposables::add)
        commentsViewModel.voteUpCommentObservable.subscribe().let(disposables::add)
        commentsViewModel.voteDownCommentObservable.subscribe().let(disposables::add)

        // disable touch events for background fragments
        view.setOnClickListener { }
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
        val view = view ?: return

        val progressbar = view.findViewById<ProgressBar>(R.id.article_comments_progressbar)
        progressbar.visibility = View.GONE

        val recyclerView = view.findViewById<RecyclerView>(R.id.article_comments_recyclerview)
        recyclerView.visibility = View.VISIBLE

        epoxyController.setComments(comments)
        recyclerView.adapter = epoxyController.adapter
        epoxyController.requestModelBuild()
        recyclerView.visibility = View.VISIBLE
    }

    private fun displayCommentsThumbnail() {
        val view = view ?: return

        val messageview = view.findViewById<TextView>(R.id.article_comments_messageview)
        messageview.setText(R.string.no_comments)
        messageview.visibility = View.VISIBLE

        val firstCommentButton = view.findViewById<View>(R.id.article_comments_firstbutton)
        firstCommentButton.visibility = View.VISIBLE
    }

    private fun onGetCommentsResponseError(response: GetCommentsResponse.Error) {
        val view = view ?: return

        val retrybutton = view.findViewById<View>(R.id.article_comments_retrybutton)
        retrybutton.visibility = View.VISIBLE

        val messageview = view.findViewById<TextView>(R.id.article_comments_messageview)
        messageview.visibility = View.VISIBLE
        messageview.text = response.json

        val progressbar = view.findViewById<ProgressBar>(R.id.article_comments_progressbar)
        progressbar.visibility = View.GONE
    }

    private fun onRetryButtonClicked(view: View) {
        val retrybutton = requireView().findViewById<View>(R.id.article_comments_retrybutton)
        retrybutton.visibility = View.GONE

        val messageview = requireView().findViewById<TextView>(R.id.article_comments_messageview)
        messageview.visibility = View.GONE

        val progressbar = requireView().findViewById<ProgressBar>(R.id.article_comments_progressbar)
        progressbar.visibility = View.VISIBLE
    }

    private fun onVoteCommentsResponse(response: VoteCommentResponse) = when(response) {
        is VoteCommentResponse.Success -> onVoteCommentsResponseSuccess(response)
        is VoteCommentResponse.Error -> onVoteCommentsResponseError(response)
    }

    private fun onVoteCommentsResponseSuccess(response: VoteCommentResponse.Success) {
        println("sas")
    }

    private fun onVoteCommentsResponseError(response: VoteCommentResponse.Error) {

    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    class Factory {
        fun build(articleId: Int): CommentsFragment {
            return CommentsFragment().apply {
                arguments.articleId = articleId
            }
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
            get() = fragmentArguments.getInt(ID) ?: -1

        companion object {
            private const val ID = "Id"
        }
    }

    class Navigator(private val router: Router) {
        fun back() = router.exit()
    }
}
