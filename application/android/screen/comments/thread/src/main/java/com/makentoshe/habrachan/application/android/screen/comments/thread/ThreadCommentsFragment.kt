package com.makentoshe.habrachan.application.android.screen.comments.thread

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.paging.map
import androidx.recyclerview.widget.ConcatAdapter
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.comment.model.GetArticleCommentsModel
import com.makentoshe.habrachan.application.android.common.comment.model.commentPagingData
import com.makentoshe.habrachan.application.android.common.comment.model.commentsPagingData
import com.makentoshe.habrachan.application.android.common.comment.model.forest.DISCUSSION_COMMENT_LEVEL_DEPTH
import com.makentoshe.habrachan.application.android.common.comment.model.forest.copy
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsSpec2
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsViewModel
import com.makentoshe.habrachan.application.android.common.fragment.BaseFragment
import com.makentoshe.habrachan.application.android.common.fragment.FragmentArguments
import com.makentoshe.habrachan.application.android.navigation.navigator.BackwardNavigator
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.ContentCommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.TitleCommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.thread.view.ThreadCommentSeparatorItemDecoration
import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.entity.commentId
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.functional.suspendFold
import kotlinx.android.synthetic.main.fragment_comments_discussion.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class ThreadCommentsFragment : BaseFragment() {

    companion object : Analytics(LogAnalytic()) {
        fun build(
            /** Comments will be downloaded for selected article. */
            articleId: Int,
            /** String that will be placed at the toolbar */
            articleTitle: String,
            /** Parent comment that may be displayed at the top. If 0 nothing will be displayed */
            commentId: Int = 0
        ) = ThreadCommentsFragment().apply {
            arguments.articleId = articleId
            arguments.articleTitle = articleTitle
            arguments.commentId = commentId
        }

        private const val VIEW_MODEL_STATE_KEY = "ViewModel"
    }

    override val arguments = Arguments(this)

    private val commentAdapter by inject<ContentCommentAdapter>()
    private val titleAdapter by inject<TitleCommentAdapter>()
    private val adapter by inject<ConcatAdapter>()

    private val backwardNavigator by inject<BackwardNavigator>()
    private val articleCommentsViewModel by inject<GetArticleCommentsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_comments_discussion, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val wasViewModelRecreated =
            articleCommentsViewModel.toString() != savedInstanceState?.getString(VIEW_MODEL_STATE_KEY)
        if (savedInstanceState == null || wasViewModelRecreated) lifecycleScope.launch(Dispatchers.IO) {
            val message = "articleId=${arguments.articleId}, commentId=${arguments.commentId}"
            capture(analyticEvent(this@ThreadCommentsFragment.javaClass.simpleName, message))
            val commentId = commentId(arguments.commentId)
            val articleId = articleId(arguments.articleId)
            val articleCommentsSpec = GetArticleCommentsSpec2.ThreadCommentsSpec(articleId, commentId)
            articleCommentsViewModel.channel.send(articleCommentsSpec)
        }

        fragment_comments_discussion_toolbar.title = arguments.articleTitle
        fragment_comments_discussion_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        fragment_comments_discussion_toolbar.setNavigationOnClickListener { backwardNavigator.toPreviousScreen() }

        val separateDecoration = ThreadCommentSeparatorItemDecoration(requireContext())
        fragment_comments_discussion_recycler.addItemDecoration(separateDecoration)

        fragment_comments_discussion_recycler.adapter = adapter

        lifecycleScope.launch(Dispatchers.IO) {
            articleCommentsViewModel.model.collectLatest { result -> onGetArticleCommentsModel(result) }
        }
    }

    private suspend fun onGetArticleCommentsModel(result: Result<GetArticleCommentsModel>) {
        result.suspendFold({ model ->
            onGetArticleCommentsModelSuccess(model)
        }, { throwable ->
            onGetArticleCommentsModelFailure(throwable)
        })
    }

    private suspend fun onGetArticleCommentsModelSuccess(model: GetArticleCommentsModel) {
        val commentId = commentId(arguments.commentId)
        commentAdapter.submitData(model.commentsPagingData(commentId, DISCUSSION_COMMENT_LEVEL_DEPTH))
        titleAdapter.submitData(model.commentPagingData(commentId).map { it.copy(level = 0) })
    }

    private fun onGetArticleCommentsModelFailure(throwable: Throwable) {
        println(throwable)
        throw throwable
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(VIEW_MODEL_STATE_KEY, articleCommentsViewModel.toString())
    }

    class Arguments(fragment: ThreadCommentsFragment) : FragmentArguments(fragment) {

        var articleId: Int
            get() = fragmentArguments.getInt(ARTICLE_ID)
            set(value) = fragmentArguments.putInt(ARTICLE_ID, value)

        var articleTitle: String
            get() = fragmentArguments.getString(ARTICLE_TITLE, "")
            set(value) = fragmentArguments.putString(ARTICLE_TITLE, value)

        var commentId: Int
            get() = fragmentArguments.getInt(COMMENT_ID)
            set(value) = fragmentArguments.putInt(COMMENT_ID, value)

        companion object {
            private const val COMMENT_ID = "CommentId"
            private const val ARTICLE_ID = "ArticleId"
            private const val ARTICLE_TITLE = "ArticleTitle"
        }
    }
}