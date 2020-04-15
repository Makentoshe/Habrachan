package com.makentoshe.habrachan.view.main.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.model.main.articles.ArticlesSearchBroadcastReceiver
import com.makentoshe.habrachan.model.main.articles.ArticlesSearchEpoxyController
import com.makentoshe.habrachan.ui.main.articles.ArticlesSearchFragmentUi
import io.reactivex.disposables.CompositeDisposable
import toothpick.ktp.delegate.inject

class ArticlesSearchFragment : Fragment() {

    private val disposables = CompositeDisposable()

    private lateinit var recyclerView: RecyclerView

    private val controller by inject<ArticlesSearchEpoxyController>()
    private val searchBroadcastReceiver by inject<ArticlesSearchBroadcastReceiver>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ArticlesSearchFragmentUi(container).buildView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById<RecyclerView>(R.id.articles_search_fragment_recycler)
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        recyclerView.adapter = controller.adapter
        controller.requestModelBuild()
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
            // Caused by: java.lang.IllegalArgumentException: Receiver not registered:
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

}

