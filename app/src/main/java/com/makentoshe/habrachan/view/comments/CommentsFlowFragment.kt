package com.makentoshe.habrachan.view.comments

import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.common.model.Comments
import com.makentoshe.habrachan.common.network.response.GetCommentsResponse
import com.makentoshe.habrachan.navigation.comments.CommentsDisplayFragmentScreen
import com.makentoshe.habrachan.navigation.comments.CommentsFlowFragmentArguments
import com.makentoshe.habrachan.navigation.comments.CommentsFragmentNavigation
import com.makentoshe.habrachan.ui.comments.CommentsFlowFragmentUi
import com.makentoshe.habrachan.viewmodel.comments.GetCommentViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import toothpick.ktp.delegate.inject
import kotlin.math.floor


class CommentsFlowFragment : Fragment() {

    private val arguments = CommentsFlowFragmentArguments(this)

    private val getCommentsViewModel by inject<GetCommentViewModel>()
//    override val sendCommentViewModel by inject<SendCommentViewModel>()

    private val disposables by inject<CompositeDisposable>()
//    private  val softKeyboardController = SoftKeyboardController()

    private val navigation by inject<CommentsFragmentNavigation>()

    private var parentId: Int = 0

    private lateinit var toolbar: Toolbar
    private lateinit var retryButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var messageView: TextView

    private lateinit var bottomSheet: ViewGroup
    private lateinit var createCommentView: EditText
    private lateinit var peekController: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return CommentsFlowFragmentUi(container).inflateView(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        retryButton = view.findViewById(R.id.comments_fragment_retrybutton)
        progressBar = view.findViewById(R.id.comments_fragment_progressbar)
        messageView = view.findViewById(R.id.comments_fragment_messageview)
        toolbar = view.findViewById(R.id.comments_fragment_toolbar)
        bottomSheet = view.findViewById(R.id.comments_fragment_bottom_sheet)
        createCommentView = view.findViewById(R.id.comments_fragment_bottom_sheet_comment_edit)
        peekController = view.findViewById(R.id.comments_fragment_bottom_sheet_peek_controller)

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

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        createCommentView.doAfterTextChanged(peekController::setText)

        // todo replace hardcoded 20f and 7f
        peekController.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            bottomSheetBehavior.peekHeight = v.height + dp2px(20f /* margins */ + 7f /* drag indicator */)
        }

        toolbar.setOnMenuItemClickListener { item ->
            return@setOnMenuItemClickListener when (item.itemId) {
                R.id.action_comment_create -> {
                    if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                    true
                }
                else -> false
            }
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

    override fun onResume() {
        super.onResume()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val containedCommentsFragment = childFragmentManager.findFragmentById(R.id.comments_fragment_container)
        if (containedCommentsFragment == null) {
            val comments = arguments.comments
            if (comments != null) {
                return displayComments(comments)
            }
            if (getCommentsViewModel.getCommentsObservable.hasValue()) {
                onGetCommentsResponse(getCommentsViewModel.getCommentsObservable.value!!)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED)
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
            if (!isStateSaved) {
                childFragmentManager.beginTransaction().add(R.id.comments_fragment_container, commentsFragment)
                    .commitNow()
            }
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

    private fun dp2px(dip: Float): Int {
        val r: Resources = resources
        return floor(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.displayMetrics)).toInt()
    }
}