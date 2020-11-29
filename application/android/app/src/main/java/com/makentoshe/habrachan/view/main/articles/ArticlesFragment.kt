package com.makentoshe.habrachan.view.main.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.broadcast.ArticlesSearchBroadcastReceiver
import com.makentoshe.habrachan.ui.main.articles.ArticlesFragmentUi
import com.makentoshe.habrachan.viewmodel.main.articles.ArticlesViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import toothpick.ktp.delegate.inject

class ArticlesFragment : Fragment() {

    private val disposables = CompositeDisposable()

    private val viewmodel by inject<ArticlesViewModel>()
    private val searchBroadcastReceiver by inject<ArticlesSearchBroadcastReceiver>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ArticlesFragmentUi(container).createView(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.articles_fragment_swipy)
        val progressBar = view.findViewById<ProgressBar>(R.id.articles_Fragment_progress)
        val recyclerView = view.findViewById<RecyclerView>(R.id.articles_fragment_recycler)
        val messageView = view.findViewById<TextView>(R.id.articles_fragment_message)
        val retryButton = view.findViewById<Button>(R.id.articles_fragment_button)

        viewmodel.adapterObservable.observeOn(AndroidSchedulers.mainThread()).subscribe { adapter ->
            recyclerView.adapter = adapter
            progressBar.visibility = View.GONE
            retryButton.visibility = View.GONE
            messageView.visibility = View.GONE
            swipeRefreshLayout.isRefreshing = false
            swipeRefreshLayout.visibility = View.VISIBLE
        }.let(disposables::add)

        viewmodel.initialErrorObservable.observeOn(AndroidSchedulers.mainThread()).subscribe {
            progressBar.visibility = View.GONE
            messageView.visibility = View.VISIBLE
            messageView.text = it.response.json
            retryButton.visibility = View.VISIBLE
        }.let(disposables::add)

        viewmodel.rangeErrorObservable.observeOn(AndroidSchedulers.mainThread()).subscribe {
            showErrorSnackbar(it.response.json)
        }.let(disposables::add)

        retryButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            messageView.visibility = View.GONE
            retryButton.visibility = View.GONE
            viewmodel.requestObserver.onNext(Unit)
        }

        swipeRefreshLayout.setOnRefreshListener{
            executeInitialRequest()
        }

        searchBroadcastReceiver.broadcastObservable.subscribe {
            progressBar.visibility = View.VISIBLE
            retryButton.visibility = View.GONE
            messageView.visibility = View.GONE
            swipeRefreshLayout.visibility = View.GONE
            viewmodel.updateUserSessionArticlesResponseSpec(it)
            viewmodel.requestObserver.onNext(Unit)
        }.let(disposables::add)

        if (savedInstanceState == null) {
            return executeInitialRequest()
        }
    }

    private fun executeInitialRequest() {
        viewmodel.requestObserver.onNext(Unit)
    }

    private fun showErrorSnackbar(message: String) {
        val snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(R.string.got_it) {
            snackbar.dismiss()
        }
        snackbar.show()
    }

    override fun onStart() {
        super.onStart()
        searchBroadcastReceiver.registerReceiver(requireContext())
    }

    override fun onStop() {
        super.onStop()
        try {
            requireContext().unregisterReceiver(searchBroadcastReceiver)
        } catch (ignoring: IllegalArgumentException) {
            // Caused by: java.lang.IllegalArgumentException: Receiver not registered
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

}