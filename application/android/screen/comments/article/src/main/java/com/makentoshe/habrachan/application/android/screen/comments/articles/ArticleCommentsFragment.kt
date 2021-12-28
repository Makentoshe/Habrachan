package com.makentoshe.habrachan.application.android.screen.comments.articles

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.makentoshe.habrachan.application.android.ExceptionController
import com.makentoshe.habrachan.application.android.ExceptionHandler
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.comment.model.GetArticleCommentsModel
import com.makentoshe.habrachan.application.android.common.comment.model.commentsPagingData
import com.makentoshe.habrachan.application.android.common.comment.model.forest.ARTICLE_COMMENT_LEVEL_DEPTH
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsSpec2
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsViewModel
import com.makentoshe.habrachan.application.android.common.fragment.BaseFragment
import com.makentoshe.habrachan.application.android.common.fragment.FragmentArguments
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.BackwardNavigator
import com.makentoshe.habrachan.application.android.screen.comments.articles.model.CommentsEmptyStateController
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.ContentCommentAdapter
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.functional.Option
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.functional.suspendFold
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class ArticleCommentsFragment : BaseFragment() {

    companion object : Analytics(LogAnalytic()) {
        fun build(articleId: ArticleId, articleTitleOption: Option<String>) = ArticleCommentsFragment().apply {
            arguments.articleId = articleId.articleId
            arguments.articleTitle = articleTitleOption.getOrNull() ?: "Loading article..."
        }

        private const val VIEW_MODEL_STATE_KEY = "ViewModel"
    }

    override val arguments = Arguments(this)

    private val adapter by inject<ContentCommentAdapter>()
    private val articleCommentsViewModel by inject<GetArticleCommentsViewModel>()
    private val backwardNavigator by inject<BackwardNavigator>()
    private val exceptionHandler by inject<ExceptionHandler>()

    private lateinit var exceptionController: ExceptionController
    private lateinit var emptyStateController: CommentsEmptyStateController

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
//    ): View? = inflater.inflate(R.layout.fragment_comments_article, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val wasViewModelRecreated =
            articleCommentsViewModel.toString() != savedInstanceState?.getString(VIEW_MODEL_STATE_KEY)
        if (savedInstanceState == null || wasViewModelRecreated) lifecycleScope.launch(Dispatchers.IO) {
            capture(
                analyticEvent(
                    this@ArticleCommentsFragment.javaClass.simpleName,
                    "articleId=${arguments.articleId}"
                )
            )
            val getArticleCommentsSpec = GetArticleCommentsSpec2.ArticleCommentsSpec(articleId(arguments.articleId))
            articleCommentsViewModel.channel.send(getArticleCommentsSpec)
        }

//        exceptionController = ExceptionController(ExceptionViewHolder(fragment_comments_article_exception))
        exceptionController.setRetryButton { adapter.retry() }

//        emptyStateController =
//            CommentsEmptyStateController(CommentsEmptyStateViewHolder(fragment_comments_article_empty_state))
        emptyStateController.buttonOnClickListener {
            Toast.makeText(requireContext(), R.string.not_implemented, Toast.LENGTH_LONG).show()
        }

//        fragment_comments_article_toolbar.title = arguments.articleTitle
//        fragment_comments_article_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
//        fragment_comments_article_toolbar.setNavigationOnClickListener { backwardNavigator.toPreviousScreen() }

//        fragment_comments_article_recycler.adapter = adapter

        lifecycleScope.launch {
            articleCommentsViewModel.model.collectLatest { result -> onGetArticleCommentsModel(result) }
        }

        adapter.addLoadStateListener(::loadStateListener)
    }

    private suspend fun onGetArticleCommentsModel(result: Result<GetArticleCommentsModel>) {
        result.suspendFold({ model ->
            onGetArticleCommentsModelSuccess(model)
        }, { throwable ->
            onGetArticleCommentsModelFailure(throwable)
        })
    }

    private suspend fun onGetArticleCommentsModelSuccess(model: GetArticleCommentsModel) {
        adapter.submitData(model.commentsPagingData(ARTICLE_COMMENT_LEVEL_DEPTH))
    }

    private fun onGetArticleCommentsModelFailure(throwable: Throwable) {
        throw throwable
    }

    private fun loadStateListener(state: CombinedLoadStates) = when (val refresh = state.refresh) {
        is LoadState.NotLoading -> {
            exceptionController.hide()
//            fragment_comments_article_progress.visibility = View.GONE
            if (state.append.endOfPaginationReached && adapter.itemCount <= 0) {
                emptyStateController.show()
//                fragment_comments_article_recycler.visibility = View.GONE
            } else {
//                fragment_comments_article_recycler.visibility = View.VISIBLE
            }
        }
        is LoadState.Loading -> {
//            exceptionController.hide()
//            emptyStateController.hide()
//            fragment_comments_article_progress.visibility = View.VISIBLE
//            fragment_comments_article_recycler.visibility = View.GONE
        }
        is LoadState.Error -> {
//            exceptionController.render(exceptionHandler.handleException(refresh.error))
//            emptyStateController.hide()
//            fragment_comments_article_progress.visibility = View.GONE
//            fragment_comments_article_recycler.visibility = View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(VIEW_MODEL_STATE_KEY, articleCommentsViewModel.toString())
    }

    class Arguments(fragment: ArticleCommentsFragment) : FragmentArguments(fragment) {

        var articleId: Int
            get() = fragmentArguments.getInt(ARTICLE_ID)
            set(value) = fragmentArguments.putInt(ARTICLE_ID, value)

        var articleTitle: String
            get() = fragmentArguments.getString(ARTICLE_TITLE, "")
            set(value) = fragmentArguments.putString(ARTICLE_TITLE, value)

        companion object {
            private const val ARTICLE_ID = "ArticleId"
            private const val ARTICLE_TITLE = "ArticleTitle"
        }
    }
}
