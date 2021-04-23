package com.makentoshe.habrachan.application.android.screen.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.ExceptionController
import com.makentoshe.habrachan.application.android.ExceptionHandler
import com.makentoshe.habrachan.application.android.ExceptionViewHolder
import com.makentoshe.habrachan.application.android.screen.articles.model.AppendArticleAdapter
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesAdapter
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesSpec
import com.makentoshe.habrachan.application.android.screen.articles.view.ArticleItemDecoration
import com.makentoshe.habrachan.application.android.screen.articles.viewmodel.ArticlesViewModel2
import com.makentoshe.habrachan.network.request.SpecType
import kotlinx.android.synthetic.main.fragment_articles.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class ArticlesFragment : CoreFragment() {

    companion object {
        fun build(page: Int = 1): ArticlesFragment {
            val fragment = ArticlesFragment()
            fragment.arguments.page = page
            return fragment
        }

        fun build(spec: SpecType) = ArticlesFragment().apply {
            arguments.spec = spec
        }
    }

    override val arguments = Arguments(this)
    private val viewModel by inject<ArticlesViewModel2>()
    private val exceptionHandler by inject<ExceptionHandler>()

    private val adapter by inject<ArticlesAdapter>()
    private val appendAdapter by inject<AppendArticleAdapter>()

    private lateinit var exceptionController: ExceptionController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_articles, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) lifecycleScope.launch {
            val requestSpec = arguments.spec ?: return@launch
            viewModel.articlesSpecChannel.send(ArticlesSpec(arguments.page, requestSpec))
        }

        lifecycleScope.launch {
            viewModel.articles.collectLatest {
                adapter.submitData(it)
            }
        }

        exceptionController = ExceptionController(ExceptionViewHolder(fragment_articles_exception))
        exceptionController.setRetryButton {
            adapter.retry()
            showLoadingState()
        }

        val itemDecoration = ArticleItemDecoration.from(requireContext())
        fragment_articles_recycler.addItemDecoration(itemDecoration)
        fragment_articles_recycler.adapter = adapter.withLoadStateFooter(appendAdapter)
        adapter.addLoadStateListener(::onLoadStateChanged)

        fragment_articles_swipe.setOnRefreshListener {
            adapter.refresh()
        }
    }

    private fun onLoadStateChanged(combinedStates: CombinedLoadStates) {
        when (val refresh = combinedStates.refresh) {
            is LoadState.Loading -> showContentLoading()
            is LoadState.NotLoading -> showContentState()
            is LoadState.Error -> showContentError(refresh.error)
        }
    }

    private fun showContentState() {
        fragment_articles_swipe.isRefreshing = false
        fragment_articles_swipe.visibility = View.VISIBLE
        fragment_articles_progress.visibility = View.GONE
    }

    private fun showContentLoading() {
        exceptionController.hide()
        if (adapter.itemCount <= 0) {
            fragment_articles_progress.visibility = View.VISIBLE
        }
    }

    private fun showContentError(throwable: Throwable) {
        fragment_articles_swipe.isRefreshing = false
        fragment_articles_progress.visibility = View.GONE
        fragment_articles_swipe.visibility = View.GONE
        exceptionController.render(exceptionHandler.handleException(throwable))
    }

    private fun showLoadingState() {
        exceptionController.hide()
        fragment_articles_progress.visibility = View.VISIBLE
        fragment_articles_swipe.visibility = View.GONE
    }

    class Arguments(fragment: ArticlesFragment) : CoreFragment.Arguments(fragment) {

        var page: Int
            get() = fragmentArguments.getInt(PAGE, 1)
            set(value) = fragmentArguments.putInt(PAGE, value)

        var spec: SpecType?
            get() = fragmentArguments.get(SPEC) as SpecType
            set(value) = fragmentArguments.putSerializable(SPEC, value)

        companion object {
            private const val PAGE = "Page"
            private const val SPEC = "Spec"
        }
    }
}
