package com.makentoshe.habrachan.application.android.screen.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.ExceptionController
import com.makentoshe.habrachan.application.android.ExceptionHandler
import com.makentoshe.habrachan.application.android.ExceptionViewHolder
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsSpec
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsViewModel
import com.makentoshe.habrachan.application.android.common.core.fragment.BaseFragment
import com.makentoshe.habrachan.application.android.common.core.fragment.FragmentArguments
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentsEmptyStateController
import com.makentoshe.habrachan.application.android.screen.comments.model.adapter.ContentCommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.navigation.CommentsNavigation
import com.makentoshe.habrachan.application.android.screen.comments.view.CommentsEmptyStateViewHolder
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.articleId
import kotlinx.android.synthetic.main.fragment_comments_article.fragment_comments_article_empty_state
import kotlinx.android.synthetic.main.fragment_comments_article.fragment_comments_article_exception
import kotlinx.android.synthetic.main.fragment_comments_article.fragment_comments_article_progress
import kotlinx.android.synthetic.main.fragment_comments_article.fragment_comments_article_recycler
import kotlinx.android.synthetic.main.fragment_comments_article.fragment_comments_article_toolbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class ArticleCommentsFragment : BaseFragment() {

    companion object : Analytics(LogAnalytic()){
        fun build(articleId: ArticleId, articleTitle: String) = ArticleCommentsFragment().apply {
            arguments.articleId = articleId.articleId
            arguments.articleTitle = articleTitle
        }

        private const val VIEW_MODEL_STATE_KEY = "ViewModel"
    }

    override val arguments = Arguments(this)

    private val adapter by inject<ContentCommentAdapter>()
    private val viewModel by inject<GetArticleCommentsViewModel>()
    private val navigation by inject<CommentsNavigation>()
    private val exceptionHandler by inject<ExceptionHandler>()

    private lateinit var exceptionController: ExceptionController
    private lateinit var emptyStateController: CommentsEmptyStateController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_comments_article, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val wasViewModelRecreated = viewModel.toString() != savedInstanceState?.getString(VIEW_MODEL_STATE_KEY)
        if (savedInstanceState == null || wasViewModelRecreated) lifecycleScope.launch(Dispatchers.IO) {
            capture(analyticEvent(this@ArticleCommentsFragment.javaClass.simpleName, "articleId=${arguments.articleId}"))
            val spec = GetArticleCommentsSpec(articleId(arguments.articleId))
            viewModel.channel.send(spec)
        }

        exceptionController = ExceptionController(ExceptionViewHolder(fragment_comments_article_exception))
        exceptionController.setRetryButton { adapter.retry() }

        emptyStateController = CommentsEmptyStateController(CommentsEmptyStateViewHolder(fragment_comments_article_empty_state))
        emptyStateController.buttonOnClickListener {
            Toast.makeText(requireContext(), R.string.not_implemented, Toast.LENGTH_LONG).show()
        }

        fragment_comments_article_toolbar.title = arguments.articleTitle
        fragment_comments_article_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        fragment_comments_article_toolbar.setNavigationOnClickListener { navigation.back() }

        fragment_comments_article_recycler.adapter = adapter

        lifecycleScope.launch {
            viewModel.comments.collectLatest {
                adapter.submitData(it)
            }
        }

        adapter.addLoadStateListener(::loadStateListener)
    }

    private fun loadStateListener(state: CombinedLoadStates) = when (val refresh = state.refresh) {
        is LoadState.NotLoading -> {
            exceptionController.hide()
            fragment_comments_article_progress.visibility = View.GONE
            if (state.append.endOfPaginationReached && adapter.itemCount <= 0) {
                emptyStateController.show()
                fragment_comments_article_recycler.visibility = View.GONE
            } else {
                fragment_comments_article_recycler.visibility = View.VISIBLE
            }
        }
        is LoadState.Loading -> {
            exceptionController.hide()
            emptyStateController.hide()
            fragment_comments_article_progress.visibility = View.VISIBLE
            fragment_comments_article_recycler.visibility = View.GONE
        }
        is LoadState.Error -> {
            exceptionController.render(exceptionHandler.handleException(refresh.error))
            emptyStateController.hide()
            fragment_comments_article_progress.visibility = View.GONE
            fragment_comments_article_recycler.visibility = View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(VIEW_MODEL_STATE_KEY, viewModel.toString())
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
