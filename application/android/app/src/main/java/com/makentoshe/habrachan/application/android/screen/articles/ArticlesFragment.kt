package com.makentoshe.habrachan.application.android.screen.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.screen.articles.viewmodel.ArticlesViewModel
import com.makentoshe.habrachan.common.broadcast.ArticlesSearchBroadcastReceiver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.articles_fragment.*
import toothpick.ktp.delegate.inject

class ArticlesFragment : Fragment() {

    private val disposables = CompositeDisposable()

    private val viewmodel by inject<ArticlesViewModel>()
    private val searchBroadcastReceiver by inject<ArticlesSearchBroadcastReceiver>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.articles_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewmodel.adapterObservable.observeOn(AndroidSchedulers.mainThread()).subscribe { adapter ->
            fragment_articles_swipe.isRefreshing = false
            fragment_articles_swipe.visibility = View.VISIBLE
            fragment_articles_progress.visibility = View.GONE
            fragment_articles_message.visibility = View.GONE
            fragment_articles_retry.visibility = View.GONE
            fragment_articles_recycler.adapter = adapter
        }.let(disposables::add)

        viewmodel.initialErrorObservable.observeOn(AndroidSchedulers.mainThread()).subscribe {
            fragment_articles_progress.visibility = View.GONE
            fragment_articles_message.visibility = View.VISIBLE
            fragment_articles_message.text = it.response.json
            fragment_articles_retry.visibility = View.VISIBLE
        }.let(disposables::add)

        viewmodel.rangeErrorObservable.observeOn(AndroidSchedulers.mainThread()).subscribe {
            showErrorSnackbar(it.response.json)
        }.let(disposables::add)

        fragment_articles_retry.setOnClickListener {
            fragment_articles_retry.visibility = View.GONE
            fragment_articles_progress.visibility = View.VISIBLE
            fragment_articles_message.visibility = View.GONE
            viewmodel.requestObserver.onNext(Unit)
        }

        fragment_articles_swipe.setOnRefreshListener { viewmodel.requestObserver.onNext(Unit) }

        searchBroadcastReceiver.broadcastObservable.subscribe {
            fragment_articles_progress.visibility = View.VISIBLE
            fragment_articles_retry.visibility = View.GONE
            fragment_articles_message.visibility = View.GONE
            fragment_articles_swipe.visibility = View.GONE
            viewmodel.updateUserSessionArticlesResponseSpec(it)
            viewmodel.requestObserver.onNext(Unit)
        }.let(disposables::add)
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