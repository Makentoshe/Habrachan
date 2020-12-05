package com.makentoshe.habrachan.application.android.screen.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.screen.articles.viewmodel.ArticlesViewModel
import com.makentoshe.habrachan.common.broadcast.ArticlesSearchBroadcastReceiver
import com.makentoshe.habrachan.common.database.session.SessionDao
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_articles_flow.*
import kotlinx.android.synthetic.main.fragment_articles_flow_content.*
import toothpick.ktp.delegate.inject

class ArticlesFragment2 : CoreFragment() {

    companion object {
        fun build(page: Int = 0): ArticlesFragment2 {
            val fragment = ArticlesFragment2()
            fragment.arguments.page = page
            return fragment
        }
    }

    override val arguments = Arguments(this)
    private val viewModel by inject<ArticlesViewModel>()
    private val disposables by inject<CompositeDisposable>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_articles_flow, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragment_articles_flow_panel.isTouchEnabled = false
        fragment_articles_flow_collapsing.isTitleEnabled = false
        fragment_articles_flow_toolbar.setOnMenuItemClickListener(::onSearchMenuItemClick)

        viewModel.adapterObservable.observeOn(AndroidSchedulers.mainThread()).subscribe {
            fragment_articles_recycler.adapter = it
        }.let(disposables::add)

        viewModel.searchSubject.subscribe {
            fragment_articles_flow_toolbar.title = it.request
        }.let(disposables::add)

        viewModel.initialObservable.observeOn(AndroidSchedulers.mainThread()).subscribe {
            it.onFailure(::onInitialFailure).onSuccess { onInitialSuccess() }
        }.let(disposables::add)

        fragment_articles_retry.setOnClickListener { onRetryClick() }

        fragment_articles_swipe.setOnRefreshListener { onSwipeRefresh() }
    }

    private fun onInitialFailure(throwable: Throwable) {
        fragment_articles_swipe.isRefreshing = false
        fragment_articles_progress.visibility = View.GONE
        fragment_articles_swipe.visibility = View.GONE

        // TODO Add normal error handling and displaying
        fragment_articles_message.text = throwable.toString()
        fragment_articles_message.visibility = View.VISIBLE
        fragment_articles_retry.visibility = View.VISIBLE
    }

    private fun onInitialSuccess() {
        fragment_articles_swipe.isRefreshing = false
        fragment_articles_swipe.visibility = View.VISIBLE
        fragment_articles_progress.visibility = View.GONE
    }

    private fun onRetryClick() {
        fragment_articles_retry.visibility = View.GONE
        fragment_articles_message.visibility = View.GONE
        fragment_articles_progress.visibility = View.VISIBLE

        onSwipeRefresh()
    }

    private fun onSwipeRefresh() {
        viewModel.searchSubject.onNext(viewModel.searchSubject.value ?: return)
    }

    private fun onSearchMenuItemClick(item: MenuItem): Boolean {
        if (item.itemId != R.id.action_search) return false
        when (fragment_articles_flow_panel?.panelState) {
            SlidingUpPanelLayout.PanelState.COLLAPSED, SlidingUpPanelLayout.PanelState.HIDDEN -> {
                fragment_articles_flow_panel?.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
                closeSoftKeyboard()
            }
            SlidingUpPanelLayout.PanelState.EXPANDED -> {
                fragment_articles_flow_panel.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            }
            else -> return false
        }
        return true
    }

    class Arguments(fragment: ArticlesFragment2) : CoreFragment.Arguments(fragment) {

        var page: Int
            get() = fragmentArguments.getInt(PAGE, 0)
            set(value) = fragmentArguments.putInt(PAGE, value)

        companion object {
            private const val PAGE = "Page"
        }
    }
}

class ArticlesFlowFragment : Fragment() {

    companion object {
        fun build(page: Int): ArticlesFlowFragment {
            val fragment = ArticlesFlowFragment()
            fragment.arguments.page = page
            return fragment
        }
    }

    val arguments = Arguments(this)

    private val disposables by inject<CompositeDisposable>()
    private val sessionDao by inject<SessionDao>()
    private val searchBroadcastReceiver by inject<ArticlesSearchBroadcastReceiver>()
//    private val articlesSearchEpoxyController by inject<ArticlesSearchEpoxyController>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return layoutInflater.inflate(R.layout.fragment_articles_flow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragment_articles_flow_panel.isTouchEnabled = false

        fragment_articles_flow_toolbar.title =
            sessionDao.get().articlesRequestSpec.toString(requireContext())
        fragment_articles_flow_toolbar.setOnMenuItemClickListener(::onSearchMenuItemClick)
        fragment_articles_flow_collapsing.isTitleEnabled = false

        searchBroadcastReceiver.broadcastObservable.subscribe {
            fragment_articles_flow_toolbar.title = it.toString(requireContext())
            fragment_articles_flow_panel.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
        }.let(disposables::add)
    }

    private fun onSearchMenuItemClick(item: MenuItem): Boolean {
        if (item.itemId != R.id.action_search) return false
        val panelState = fragment_articles_flow_panel?.panelState
        if (panelState != SlidingUpPanelLayout.PanelState.COLLAPSED) {
            fragment_articles_flow_panel?.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
            closeSoftKeyboard()
            return true
        }
        if (panelState != SlidingUpPanelLayout.PanelState.EXPANDED) {
            fragment_articles_flow_panel.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
//            articlesSearchEpoxyController.requestModelBuild()
            return true
        }
        return false
    }

    override fun onStart() {
        super.onStart()
        searchBroadcastReceiver.registerReceiver(requireContext())
    }

    override fun onStop() {
        super.onStop()
        try {
            requireContext().unregisterReceiver(searchBroadcastReceiver)
        } catch (ignoring: IllegalArgumentException) { // Caused by: java.lang.IllegalArgumentException: Receiver not registered
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireFragmentManager().fragments.filterIsInstance<ArticlesFragment>().forEach {
            requireFragmentManager().beginTransaction().remove(it).commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    private fun closeSoftKeyboard() {
        val imm =
            requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus ?: View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    class Arguments(private val articlesFlowFragment: ArticlesFlowFragment) {

        init {
            val fragment = articlesFlowFragment as Fragment
            if (fragment.arguments == null) {
                fragment.arguments = Bundle()
            }
        }

        private val fragmentArguments: Bundle
            get() = articlesFlowFragment.requireArguments()

        var page: Int
            get() = fragmentArguments.getInt(PAGE)
            set(value) = fragmentArguments.putInt(PAGE, value)

        companion object {
            private const val PAGE = "Page"
        }
    }
}
