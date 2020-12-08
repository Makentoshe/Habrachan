package com.makentoshe.habrachan.application.android.screen.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.ExceptionHandler
import com.makentoshe.habrachan.application.android.screen.articles.viewmodel.ArticlesViewModel
import com.makentoshe.habrachan.network.request.GetArticlesRequest
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_articles.*
import kotlinx.android.synthetic.main.fragment_articles_content.*
import kotlinx.android.synthetic.main.fragment_articles_panel.*
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
    private val disposables by inject<CompositeDisposable>()
    private val exceptionHandler by inject<ExceptionHandler>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_articles, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onViewCreatedContent()
        onViewCreatedPanel()

        fragment_articles_flow_panel.isTouchEnabled = false
        fragment_articles_flow_collapsing.isTitleEnabled = false
        fragment_articles_flow_toolbar.setOnMenuItemClickListener(::onSearchMenuItemClick)

        viewModel.searchSubject.subscribe {
            fragment_articles_flow_toolbar.title = deserializeSpec(it)
        }.let(disposables::add)
    }

    private fun onViewCreatedContent() {
        viewModel.adapterObservable.observeOn(AndroidSchedulers.mainThread()).subscribe {
            fragment_articles_recycler.adapter = it
        }.let(disposables::add)

        viewModel.searchSubject.observeOn(AndroidSchedulers.mainThread()).subscribe {
            onRetryClickView()
        }.let(disposables::add)

        viewModel.initialObservable.observeOn(AndroidSchedulers.mainThread()).subscribe {
            it.onFailure(::onInitialFailure).onSuccess { onInitialSuccessView() }
        }.let(disposables::add)

        fragment_articles_retry.setOnClickListener {
            onRetryClickView()
            viewModel.searchSubject.onNext(viewModel.searchSubject.value ?: return@setOnClickListener)
        }

        fragment_articles_swipe.setOnRefreshListener {
            viewModel.searchSubject.onNext(viewModel.searchSubject.value ?: return@setOnRefreshListener)
        }
    }

    private fun onViewCreatedPanel() {
        fragment_articles_category_toggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener // ignore uncheck call
            closeSearchPanel()
            onCategoryChecked(checkedId)
        }
        fragment_articles_category_toggle.check(R.id.fragment_articles_category_all)
    }

    private fun onCategoryChecked(checkedId: Int) = when (checkedId) {
        R.id.fragment_articles_category_all -> {
            viewModel.searchSubject.onNext(GetArticlesRequest.Spec.All())
        }
        R.id.fragment_articles_category_interesting -> {
            viewModel.searchSubject.onNext(GetArticlesRequest.Spec.Interesting())
        }
        R.id.fragment_articles_category_top -> {
            viewModel.searchSubject.onNext(GetArticlesRequest.Spec.Top(GetArticlesRequest.Spec.Top.Type.Daily))
        }
        else -> throw IllegalArgumentException(checkedId.toString())
    }

    private fun onInitialFailure(throwable: Throwable) {
        fragment_articles_swipe.isRefreshing = false
        fragment_articles_progress.visibility = View.GONE
        fragment_articles_swipe.visibility = View.GONE

        val entry = exceptionHandler.handleException(throwable)
        fragment_articles_title.text = entry.title
        fragment_articles_title.visibility = View.VISIBLE

        fragment_articles_message.text = entry.message
        fragment_articles_message.visibility = View.VISIBLE

        fragment_articles_retry.visibility = View.VISIBLE
    }

    private fun onInitialSuccessView() {
        fragment_articles_recycler.post { fragment_articles_recycler.scrollToPosition(0) }
        fragment_articles_swipe.isRefreshing = false
        fragment_articles_swipe.visibility = View.VISIBLE
        fragment_articles_progress.visibility = View.GONE
    }

    private fun onRetryClickView() {
        fragment_articles_retry.visibility = View.GONE
        fragment_articles_message.visibility = View.GONE
        fragment_articles_title.visibility = View.GONE
        fragment_articles_progress.visibility = View.VISIBLE
        fragment_articles_swipe.visibility = View.GONE
    }

    private fun onSearchMenuItemClick(item: MenuItem): Boolean {
        if (item.itemId != R.id.action_search) return false
        when (fragment_articles_flow_panel?.panelState) {
            SlidingUpPanelLayout.PanelState.COLLAPSED, SlidingUpPanelLayout.PanelState.HIDDEN -> {
                fragment_articles_flow_panel?.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
            }
            SlidingUpPanelLayout.PanelState.EXPANDED -> closeSearchPanel()
            else -> return false
        }
        return true
    }

    private fun closeSearchPanel() {
        fragment_articles_flow_panel?.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        closeSoftKeyboard()
    }

    private fun deserializeSpec(spec: GetArticlesRequest.Spec): String = when(spec) {
        is GetArticlesRequest.Spec.All -> {
            requireContext().getString(R.string.articles_type_all)
        }
        is GetArticlesRequest.Spec.Interesting -> {
            requireContext().getString(R.string.articles_type_interesting)
        }
        is GetArticlesRequest.Spec.Top -> {
            val type = when(spec.type) {
                GetArticlesRequest.Spec.Top.Type.AllTime -> {
                    requireContext().getString(R.string.articles_top_type_alltime)
                }
                GetArticlesRequest.Spec.Top.Type.Yearly -> {
                    requireContext().getString(R.string.articles_top_type_yearly)
                }
                GetArticlesRequest.Spec.Top.Type.Monthly -> {
                    requireContext().getString(R.string.articles_top_type_monthly)
                }
                GetArticlesRequest.Spec.Top.Type.Weekly -> {
                    requireContext().getString(R.string.articles_top_type_weekly)
                }
                GetArticlesRequest.Spec.Top.Type.Daily -> {
                    requireContext().getString(R.string.articles_top_type_daily)
                }
            }
            requireContext().getString(R.string.articles_top_preposition, type)
        }
    }

    class Arguments(fragment: ArticlesFragment) : CoreFragment.Arguments(fragment) {

        var page: Int
            get() = fragmentArguments.getInt(PAGE, 0)
            set(value) = fragmentArguments.putInt(PAGE, value)

        companion object {
            private const val PAGE = "Page"
        }
    }
}
