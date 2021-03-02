package com.makentoshe.habrachan.application.android.screen.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
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
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticleAdapter
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesSpec
import com.makentoshe.habrachan.application.android.screen.articles.view.ArticleItemDecoration
import com.makentoshe.habrachan.application.android.screen.articles.viewmodel.ArticlesViewModel
import com.makentoshe.habrachan.network.request.SpecType
import com.makentoshe.habrachan.network.request.TopSpecType
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.articles_fragment.view.*
import kotlinx.android.synthetic.main.fragment_article_toolbar.*
import kotlinx.android.synthetic.main.fragment_articles.*
import kotlinx.android.synthetic.main.fragment_articles_content.*
import kotlinx.android.synthetic.main.fragment_articles_panel.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class ArticlesFragment : CoreFragment() {

    companion object {
        fun build(page: Int = 0): ArticlesFragment {
            val fragment = ArticlesFragment()
            fragment.arguments.page = page
            return fragment
        }
    }

    override val arguments = Arguments(this)
    private val viewModel by inject<ArticlesViewModel>()
    private val exceptionHandler by inject<ExceptionHandler>()

    private val adapter by inject<ArticleAdapter>()
    private val appendAdapter by inject<AppendArticleAdapter>()

    private lateinit var exceptionController: ExceptionController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_articles, container, false)

    // TODO add controller for the panel view
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        exceptionController = ExceptionController(ExceptionViewHolder(fragment_articles_exception))
        exceptionController.setRetryButton {
            adapter.retry()
            showLoadingState()
        }

        if (savedInstanceState == null) lifecycleScope.launch {
            val requestSpec = SpecType.All
            updateArticleRequestSpecViews(requestSpec)
            viewModel.articles(ArticlesSpec(arguments.page, requestSpec)).collectLatest {
                adapter.submitData(it)
            }
        }

        val itemDecoration = ArticleItemDecoration.from(requireContext())
        fragment_articles_recycler.addItemDecoration(itemDecoration)
        fragment_articles_recycler.adapter = adapter.withLoadStateFooter(appendAdapter)
        adapter.addLoadStateListener(::onLoadStateChanged)

        fragment_articles_swipe.setOnRefreshListener {
            adapter.refresh()
        }

        fragment_articles_panel.isTouchEnabled = false
        fragment_articles_toolbar.setOnMenuItemClickListener(::onSearchMenuItemClick)
        fragment_articles_collapsing.isTitleEnabled = false
        fragment_articles_category_toggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener // ignore uncheck call
            closeSearchPanel()
            onCategoryChecked(checkedId)
            showLoadingState()
        }
    }

    private fun updateArticleRequestSpecViews(spec: SpecType) = when (spec) {
        is SpecType.All -> {
            fragment_articles_toolbar.setTitle(R.string.articles_type_all)
            fragment_articles_category_toggle.check(R.id.fragment_articles_category_all)
        }
        is SpecType.Interesting -> {
            fragment_articles_toolbar.setTitle(R.string.articles_type_interesting)
            fragment_articles_category_toggle.check(R.id.fragment_articles_category_interesting)
        }
        is SpecType.Top -> {
            val type = when (spec.type) {
                TopSpecType.Alltime -> {
                    requireContext().getString(R.string.articles_top_type_alltime)
                }
                TopSpecType.Yearly -> {
                    requireContext().getString(R.string.articles_top_type_yearly)
                }
                TopSpecType.Monthly -> {
                    requireContext().getString(R.string.articles_top_type_monthly)
                }
                TopSpecType.Weekly -> {
                    requireContext().getString(R.string.articles_top_type_weekly)
                }
                TopSpecType.Daily -> {
                    requireContext().getString(R.string.articles_top_type_daily)
                }
            }
            fragment_articles_toolbar.title =
                requireContext().getString(R.string.articles_top_preposition, type)
            fragment_articles_category_toggle.check(R.id.fragment_articles_category_top)
        }
    }

    private fun onLoadStateChanged(combinedStates: CombinedLoadStates) {
        when (val refresh = combinedStates.refresh) {
            is LoadState.Loading -> showContentLoading()
            is LoadState.NotLoading -> showContentState()
            is LoadState.Error -> showContentError(refresh.error)
        }
    }

    private fun updateAdapterContent(spec: SpecType) = lifecycleScope.launch {
        updateArticleRequestSpecViews(spec)

        val articlesSpec = ArticlesSpec(arguments.page, spec)
        viewModel.articles(articlesSpec).collectLatest {
            adapter.submitData(it)
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

    private fun onSearchMenuItemClick(item: MenuItem): Boolean {
        if (item.itemId != R.id.action_search) return false
        when (fragment_articles_panel?.panelState) {
            SlidingUpPanelLayout.PanelState.COLLAPSED, SlidingUpPanelLayout.PanelState.HIDDEN -> {
                fragment_articles_panel?.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
            }
            SlidingUpPanelLayout.PanelState.EXPANDED -> closeSearchPanel()
            else -> return false
        }
        return true
    }

    private fun closeSearchPanel() {
        fragment_articles_panel?.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        closeSoftKeyboard()
    }

    private fun onCategoryChecked(checkedId: Int) = when (checkedId) {
        R.id.fragment_articles_category_all -> {
            updateAdapterContent(SpecType.All)
        }
        R.id.fragment_articles_category_interesting -> {
            updateAdapterContent(SpecType.Interesting)
        }
        R.id.fragment_articles_category_top -> {
            updateAdapterContent(SpecType.Top(TopSpecType.Daily))
        }
        else -> throw IllegalArgumentException(checkedId.toString())
    }

    // TODO add initial spec as argument
    class Arguments(fragment: ArticlesFragment) : CoreFragment.Arguments(fragment) {

        var page: Int
            get() = fragmentArguments.getInt(PAGE, 0)
            set(value) = fragmentArguments.putInt(PAGE, value)

        companion object {
            private const val PAGE = "Page"
        }
    }
}
