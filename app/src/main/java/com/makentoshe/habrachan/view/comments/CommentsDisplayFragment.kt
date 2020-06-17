package com.makentoshe.habrachan.view.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.common.model.Comments
import com.makentoshe.habrachan.common.model.tree.Tree
import com.makentoshe.habrachan.common.network.response.VoteCommentResponse
import com.makentoshe.habrachan.common.ui.SnackbarErrorController
import com.makentoshe.habrachan.model.comments.CommentActionProvider
import com.makentoshe.habrachan.model.comments.CommentsEpoxyController
import com.makentoshe.habrachan.navigation.comments.CommentsDisplayFragmentArguments
import com.makentoshe.habrachan.navigation.comments.CommentsFragmentNavigation
import com.makentoshe.habrachan.ui.comments.CommentsFragmentUi
import com.makentoshe.habrachan.viewmodel.comments.VoteCommentViewModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import toothpick.ktp.delegate.inject

class CommentsDisplayFragment : Fragment(), CommentActionProvider {

    private val arguments by inject<CommentsDisplayFragmentArguments>()
    private val navigation by inject<CommentsFragmentNavigation>()
    private val disposables by inject<CompositeDisposable>()
    private val epoxyController by inject<CommentsEpoxyController>()
    private val voteCommentViewModel by inject<VoteCommentViewModel>()

    private lateinit var recyclerView: RecyclerView

    private val inspectUserSubject = PublishSubject.create<Comment>()
    override val inspectUserObserver: Observer<Comment> = inspectUserSubject

    private val voteUpCommentSubject = PublishSubject.create<Comment>()
    override val voteUpCommentObserver: Observer<Comment> = voteUpCommentSubject

    private val voteDownCommentSubject = PublishSubject.create<Comment>()
    override val voteDownCommentObserver: Observer<Comment> = voteDownCommentSubject

    private val replyCommentSubject = PublishSubject.create<Comment>()
    override val replyCommentObserver: Observer<Comment> = replyCommentSubject

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return CommentsFragmentUi(container).inflateView(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.comments_fragment_recyclerview)

        displayComments(Comments.convertCommentsListToCommentsTree(arguments.comments))

        // disable touch events for background fragments
        view.setOnClickListener { }

        // requests comment's score change - increase score
        voteUpCommentSubject.map { it.id }.safeSubscribe(voteCommentViewModel.voteUpCommentObserver)
        // callback for comment's score change - displays new score
        voteCommentViewModel.voteUpCommentObservable.observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onVoteCommentsResponse).let(disposables::add)

        // requests comment's score change - decrease score
        voteDownCommentSubject.map { it.id }.safeSubscribe(voteCommentViewModel.voteDownCommentObserver)
        // callback for comment's score change - displays new score
        voteCommentViewModel.voteDownCommentObservable.observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onVoteCommentsResponse).let(disposables::add)

        // requests new screen open with the comment owner profile
        inspectUserSubject.subscribe { navigation.toUserScreen(it.author.login) }.let(disposables::add)

        // requests new screen for replying to the last comment in thread
        replyCommentSubject.subscribe(::onReplyCommentRequest).let(disposables::add)
    }

    private fun displayComments(tree: Tree<Comment>) {
        epoxyController.setComments(tree)
        recyclerView.adapter = epoxyController.adapter
        epoxyController.requestModelBuild()
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

    private fun onReplyCommentRequest(comment: Comment) {
        val commentsTree = epoxyController.getCommentsTree()
        val node = commentsTree.findNode { it == comment }
        val path = commentsTree.pathToRoot(node!!).reversed()
        navigation.toReplyScreen(path.map { it.value }, arguments.articleId)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}
