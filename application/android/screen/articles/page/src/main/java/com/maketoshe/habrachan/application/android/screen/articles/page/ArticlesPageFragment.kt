package com.maketoshe.habrachan.application.android.screen.articles.page

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.articles.viewmodel.GetArticlesViewModel
import com.makentoshe.habrachan.application.android.common.binding.viewBinding
import com.makentoshe.habrachan.application.android.common.fragment.BindableBaseFragment
import com.makentoshe.habrachan.application.android.common.fragment.FragmentArguments
import com.makentoshe.habrachan.application.android.exception.ExceptionEntry
import com.makentoshe.habrachan.application.android.exception.ExceptionHandler
import com.makentoshe.habrachan.application.common.arena.ArenaException
import com.maketoshe.habrachan.application.android.screen.articles.page.databinding.FragmentPageArticlesBinding
import com.maketoshe.habrachan.application.android.screen.articles.page.model.ArticlesFooterAdapter
import com.maketoshe.habrachan.application.android.screen.articles.page.model.ArticlesPageAdapter
import com.maketoshe.habrachan.application.android.screen.articles.page.view.ArticlesPageItemDecoration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class ArticlesPageFragment : BindableBaseFragment() {

    companion object : Analytics(LogAnalytic()) {

        fun build(index: Int) = ArticlesPageFragment().apply {
            arguments.index = index
        }
    }

    override val arguments = Arguments(this)
    override val binding: FragmentPageArticlesBinding by viewBinding()

    private val exceptionHandler by inject<ExceptionHandler>()
    private val getArticlesViewModel by inject<GetArticlesViewModel>()
    private val adapter by inject<ArticlesPageAdapter>()
    private val footerAdapter by inject<ArticlesFooterAdapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        capture(analyticEvent { "Create(index=${arguments.index})" })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.fragmentPageArticlesRecycler.adapter = adapter.withLoadStateFooter(footerAdapter)
        binding.fragmentPageArticlesRecycler.addItemDecoration(ArticlesPageItemDecoration.from(requireContext()))
        adapter.addLoadStateListener(::onLoadStateChanged)

        binding.fragmentPageArticlesRetry.setOnClickListener { adapter.retry() }
        binding.fragmentPageArticlesSwipe.setOnRefreshListener { adapter.refresh() }

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

        binding.fragmentPageArticlesProgress.visibility = View.VISIBLE
        binding.fragmentPageArticlesTitle.visibility = View.GONE
        binding.fragmentPageArticlesMessage.visibility = View.GONE
        binding.fragmentPageArticlesRetry.visibility = View.GONE
    }

    private fun onLoadStateChangedContent() {
        binding.fragmentPageArticlesSwipe.isRefreshing = false
        binding.fragmentPageArticlesSwipe.visibility = View.VISIBLE
        binding.fragmentPageArticlesProgress.visibility = View.GONE
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
        binding.fragmentPageArticlesSwipe.isRefreshing = false
        binding.fragmentPageArticlesSwipe.visibility = View.GONE
        binding.fragmentPageArticlesProgress.visibility = View.GONE

        binding.fragmentPageArticlesTitle.visibility = View.VISIBLE
        binding.fragmentPageArticlesTitle.text = exceptionEntry.title

        binding.fragmentPageArticlesMessage.visibility = View.VISIBLE
        binding.fragmentPageArticlesMessage.text = exceptionEntry.message

        binding.fragmentPageArticlesRetry.visibility = View.VISIBLE
    }

    class Arguments(fragment: ArticlesPageFragment) : FragmentArguments(fragment) {

        var index: Int
            get() = fragmentArguments.getInt(INDEX)
            set(value) = fragmentArguments.putInt(INDEX, value)

        companion object {
            private const val INDEX = "Index"
            private const val SPEC = "Spec"
        }
    }
}
