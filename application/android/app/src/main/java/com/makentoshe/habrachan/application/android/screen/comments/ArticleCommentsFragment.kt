package com.makentoshe.habrachan.application.android.screen.comments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.*
import com.makentoshe.habrachan.application.android.screen.comments.model.CommentsDataSource
import com.makentoshe.habrachan.application.android.screen.comments.model.ReplyCommentPagingAdapter
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.CommentsViewModel
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

        fun build(articleId: Int) = ArticleCommentsFragment().apply {
            arguments.articleId = articleId
        }
    }

    override val arguments = Arguments(this)
    private val adapter by inject<ReplyCommentPagingAdapter>()
    private val viewModel by inject<CommentsViewModel>()
    private val exceptionHandler by inject<ExceptionHandler>()

    private lateinit var exceptionController: ExceptionController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_comments_article, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        exceptionController = ExceptionController(ExceptionViewHolder(fragment_comments_article_exception))
        exceptionController.setRetryButton { adapter.retry() }

        if (savedInstanceState == null) lifecycleScope.launch { // init article comments loading
            viewModel.sendSpecChannel.send(CommentsDataSource.CommentsSpec(arguments.articleId))
        }

        fragment_comments_article_toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        fragment_comments_article_toolbar.setNavigationOnClickListener {
            Toast.makeText(requireContext(), "Not implemented", Toast.LENGTH_LONG).show()
        }

        val dividerItemDecoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        val dividerDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.drawable_divider)
        dividerItemDecoration.setDrawable(dividerDrawable!!)
        fragment_comments_article_recycler.addItemDecoration(dividerItemDecoration)
        fragment_comments_article_recycler.adapter = adapter

        lifecycleScope.launch {
            viewModel.comments.collectLatest { adapter.submitData(it) }
        }

        adapter.addLoadStateListener {
            when (val state = it.refresh) {
                is LoadState.Loading -> {
                    fragment_comments_article_progress.visibility = View.VISIBLE
                    fragment_comments_article_recycler.visibility = View.GONE
                    exceptionController.disable()
                }
                is LoadState.NotLoading -> {
                    fragment_comments_article_progress.visibility = View.GONE
                    fragment_comments_article_recycler.visibility = View.VISIBLE
                    exceptionController.disable()
                }
                is LoadState.Error -> {
                    fragment_comments_article_progress.visibility = View.GONE
                    exceptionController.render(exceptionHandler.handleException(state.error))
                }
            }
        }
    }

    class Arguments(fragment: ArticleCommentsFragment) : CoreFragment.Arguments(fragment) {

        var articleId: Int
            get() = fragmentArguments.getInt(ARTICLE_ID)
            set(value) = fragmentArguments.putInt(ARTICLE_ID, value)

        companion object {
            private const val ARTICLE_ID = "ArticleId"
        }
    }
}
