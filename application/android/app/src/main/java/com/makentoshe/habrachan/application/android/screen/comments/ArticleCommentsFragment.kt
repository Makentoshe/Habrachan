package com.makentoshe.habrachan.application.android.screen.comments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.*
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentAdapter
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentsEmptyStateController
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentsSpec
import com.makentoshe.habrachan.application.android.screen.comments.navigation.ArticleCommentsNavigation
import com.makentoshe.habrachan.application.android.screen.comments.view.CommentsEmptyStateViewHolder
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.ArticleCommentsViewModel
import kotlinx.android.synthetic.main.fragment_comments_article.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class ArticleCommentsFragment : CoreFragment() {

    companion object {
        fun capture(level: Int, message: () -> String) {
            if (!BuildConfig.DEBUG) return
            Log.println(level, "ArticleCommentsFragment", message())
        }

        fun build(articleId: Int, articleTitle: String) = ArticleCommentsFragment().apply {
            arguments.articleId = articleId
            arguments.articleTitle = articleTitle
        }
    }

    override val arguments = Arguments(this)
    private val adapter by inject<CommentAdapter>()
    private val viewModel by inject<ArticleCommentsViewModel>()
    private val navigation by inject<ArticleCommentsNavigation>()
    private val exceptionHandler by inject<ExceptionHandler>()

    private lateinit var exceptionController: ExceptionController
    private lateinit var emptyStateController: CommentsEmptyStateController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_comments_article, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) lifecycleScope.launch {
            val spec = CommentsSpec(arguments.articleId)
            viewModel.sendSpecChannel.send(spec)
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

    class Arguments(fragment: ArticleCommentsFragment) : CoreFragment.Arguments(fragment) {

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
