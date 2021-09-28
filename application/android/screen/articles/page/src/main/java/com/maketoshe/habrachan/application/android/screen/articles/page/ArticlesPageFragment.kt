package com.maketoshe.habrachan.application.android.screen.articles.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.articles.viewmodel.GetArticlesViewModel
import com.makentoshe.habrachan.application.android.common.fragment.BaseFragment
import com.makentoshe.habrachan.application.android.common.fragment.FragmentArguments
import com.makentoshe.habrachan.application.android.exception.ExceptionEntry
import com.makentoshe.habrachan.application.android.exception.ExceptionHandler
import com.makentoshe.habrachan.application.common.arena.ArenaException
import com.makentoshe.habrachan.network.request.SpecType
import com.maketoshe.habrachan.application.android.screen.articles.page.model.ArticlesFooterAdapter
import com.maketoshe.habrachan.application.android.screen.articles.page.model.ArticlesPageAdapter
import com.maketoshe.habrachan.application.android.screen.articles.page.view.ArticlesPageItemDecoration
import kotlinx.android.synthetic.main.fragment_page_articles.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class ArticlesPageFragment : BaseFragment() {

    companion object : Analytics(LogAnalytic()) {

        fun build(spec: SpecType) = ArticlesPageFragment().apply {
            arguments.spec = spec
        }
    }

    override val arguments = Arguments(this)

    private val exceptionHandler by inject<ExceptionHandler>()
    private val getArticlesViewModel by inject<GetArticlesViewModel>()
    private val adapter by inject<ArticlesPageAdapter>()
    private val footerAdapter by inject<ArticlesFooterAdapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        capture(analyticEvent { "Create(${arguments.spec})" })
    }

    override fun internalOnCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View {
        return inflater.inflate(R.layout.fragment_page_articles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragment_page_articles_recycler.adapter = adapter.withLoadStateFooter(footerAdapter)
        fragment_page_articles_recycler.addItemDecoration(ArticlesPageItemDecoration.from(requireContext()))
        adapter.addLoadStateListener(::onLoadStateChanged)

        fragment_page_articles_retry.setOnClickListener { adapter.retry() }
        fragment_page_articles_swipe.setOnRefreshListener { adapter.refresh() }

        lifecycleScope.launch(Dispatchers.IO) {
            getArticlesViewModel.pagingData.collectLatest { data ->
                adapter.submitData(data)
            }
        }
    }

    private fun onLoadStateChanged(combinedStates: CombinedLoadStates) = when (val refresh = combinedStates.also { println(it) }.refresh) {
        is LoadState.Loading -> onLoadStateChangedLoading()
        is LoadState.NotLoading -> onLoadStateChangedContent()
        is LoadState.Error -> onLoadStateChangedError(refresh.error)
    }

    private fun onLoadStateChangedLoading() {
        if (adapter.itemCount > 0) return

        fragment_page_articles_progress.visibility = View.VISIBLE
        fragment_page_articles_title.visibility = View.GONE
        fragment_page_articles_message.visibility = View.GONE
        fragment_page_articles_retry.visibility = View.GONE
    }

    private fun onLoadStateChangedContent() {
        fragment_page_articles_swipe.isRefreshing = false
        fragment_page_articles_swipe.visibility = View.VISIBLE
        fragment_page_articles_progress.visibility = View.GONE
    }

    private fun onLoadStateChangedError(throwable: Throwable) {
        val arenaException = throwable as ArenaException
        val getArticlesException = arenaException.cause?.cause
        if (getArticlesException != null) exceptionHandler.handle(getArticlesException) else {
            val title = getString(R.string.articles_initial_exception_title)
            ExceptionEntry(title, throwable.toString())
        }.let(::onLoadStateChangedError)
    }

    private fun onLoadStateChangedError(exceptionEntry: ExceptionEntry) {
        fragment_page_articles_swipe.isRefreshing = false
        fragment_page_articles_progress.visibility = View.GONE
        fragment_page_articles_swipe.visibility = View.GONE

        fragment_page_articles_title.visibility = View.VISIBLE
        fragment_page_articles_title.text = exceptionEntry.title

        fragment_page_articles_message.visibility = View.VISIBLE
        fragment_page_articles_message.text = exceptionEntry.message

        fragment_page_articles_retry.visibility = View.VISIBLE
    }

    class Arguments(fragment: ArticlesPageFragment) : FragmentArguments(fragment) {

        var spec: SpecType
            get() = fragmentArguments.get(SPEC) as SpecType
            set(value) = fragmentArguments.putSerializable(SPEC, value)

        companion object {
            private const val SPEC = "Spec"
        }
    }
}
