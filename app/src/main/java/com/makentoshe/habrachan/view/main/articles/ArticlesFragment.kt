package com.makentoshe.habrachan.view.main.articles

import android.content.IntentFilter
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.entity.post.ArticlesResponse
import com.makentoshe.habrachan.model.main.articles.AppBarStateChangeListener
import com.makentoshe.habrachan.model.main.articles.AppbarStateBroadcastReceiver
import com.makentoshe.habrachan.ui.main.articles.ArticlesFragmentUi
import com.makentoshe.habrachan.viewmodel.main.articles.ArticlesControllerViewModel
import com.makentoshe.habrachan.viewmodel.main.articles.ArticlesViewModel
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection
import io.reactivex.disposables.CompositeDisposable
import toothpick.ktp.delegate.inject
import kotlin.math.max

class ArticlesFragment : Fragment() {

    private var currentPage = 0
    private val disposables = CompositeDisposable()
    private val articlesViewModel by inject<ArticlesViewModel>()
    private val articlesControllerViewModel by inject<ArticlesControllerViewModel>()
    private val appbarStateBroadcastReceiver = AppbarStateBroadcastReceiver()
    
    private val arguments: ArticlesFlowFragment.Arguments
        get() = (requireParentFragment() as ArticlesFlowFragment).arguments

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filter = IntentFilter(AppBarStateChangeListener.State::class.java.simpleName)
        requireContext().registerReceiver(appbarStateBroadcastReceiver, filter)
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ArticlesFragmentUi(container).createView(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        currentPage = SavedInstanceState(savedInstanceState).page ?: arguments.page

        val recyclerView = view.findViewById<RecyclerView>(R.id.articles_fragment_recycler)
        val swipyRefreshView = view.findViewById<SwipyRefreshLayout>(R.id.articles_fragment_swipy)
        val progressbar = view.findViewById<ProgressBar>(R.id.articles_Fragment_progress)
        val errorMessage = view.findViewById<TextView>(R.id.articles_fragment_message)
        val retryButton = view.findViewById<Button>(R.id.articles_fragment_button)

        appbarStateBroadcastReceiver.observable.subscribe {
            try {
                swipyRefreshView.isEnabled = it == AppBarStateChangeListener.State.EXPANDED && !swipyRefreshView.canChildScrollUp()
            } catch (npe: NullPointerException) {
                // in first iteration the canChildScrollUp can throw NPE
                // because inner views does not initialized properly yet (may be)
            }
        }.let(disposables::add)

        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!swipyRefreshView.canChildScrollDown()) swipyRefreshView.isEnabled = true
            }
        })

        articlesViewModel.articlesObservable.subscribe { response ->
            swipyRefreshView.isRefreshing = false
            when (response) {
                is ArticlesResponse.Success -> {
                    progressbar.visibility = View.GONE
                    swipyRefreshView.visibility = View.VISIBLE
                    val lastItem = articlesControllerViewModel.adapter.itemCount
                    articlesControllerViewModel.appendAndRequestBuild(response.data)
                    if (currentPage > 1) {
                        val scroller = object : LinearSmoothScroller(view.context) {
                            override fun getVerticalSnapPreference() = SNAP_TO_START
                            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics) = .2f
                        }
                        scroller.targetPosition = max(lastItem - 1, 0)
                        recyclerView.layoutManager?.startSmoothScroll(scroller)
                    }
                    currentPage = response.nextPage.int
                }
                is ArticlesResponse.Error -> {
                    if (currentPage == 1){
                        errorMessage.text = response.json
                        retryButton.visibility = View.VISIBLE
                        errorMessage.visibility = View.VISIBLE
                        progressbar.visibility = View.GONE
                    } else {
                        showErrorSnackbar(response.json)
                    }
                }
            }
        }.let(disposables::add)

        retryButton.setOnClickListener {
            retryButton.visibility = View.GONE
            errorMessage.visibility = View.GONE
            progressbar.visibility = View.VISIBLE
            val request = articlesViewModel.createRequestAll(currentPage)
            articlesViewModel.requestObserver.onNext(request)
        }

        swipyRefreshView.setOnRefreshListener { direction ->
            when (direction) {
                SwipyRefreshLayoutDirection.TOP -> {
                    currentPage = arguments.page // as usual 1
                    val request = articlesViewModel.createRequestAll(currentPage)
                    articlesViewModel.requestObserver.onNext(request)
                    articlesControllerViewModel.clear()
                }
                SwipyRefreshLayoutDirection.BOTTOM -> {
                    val request = articlesViewModel.createRequestAll(currentPage)
                    articlesViewModel.requestObserver.onNext(request)
                }
                else -> Unit
            }
        }

        swipyRefreshView.setDistanceToTriggerSync(128)
        recyclerView.adapter = articlesControllerViewModel.adapter

        if (savedInstanceState == null) {
            val initialRequest = articlesViewModel.createRequestAll(arguments.page)
            articlesViewModel.requestObserver.onNext(initialRequest)
        } else {
            if (articlesControllerViewModel.shouldRequestBuild()) {
                articlesControllerViewModel.requestBuild()
                progressbar.visibility = View.GONE
                swipyRefreshView.visibility = View.VISIBLE
            }
        }
    }

    private fun showErrorSnackbar(message: String) {
        val snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(R.string.got_it) {
            snackbar.dismiss()
        }
        snackbar.show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        SavedInstanceState(outState).page = currentPage
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    private class SavedInstanceState(private val savedInstanceState: Bundle?) {

        var page: Int?
            get() = savedInstanceState?.getInt(PAGE)
            set(value) {
                if (savedInstanceState == null || value == null) return
                savedInstanceState.putInt(PAGE, value)
            }

        companion object {
            private const val PAGE = "Page"
        }
    }
}