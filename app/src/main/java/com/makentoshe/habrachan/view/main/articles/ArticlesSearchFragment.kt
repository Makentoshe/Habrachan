package com.makentoshe.habrachan.view.main.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.model.main.articles.ArticlesSearchEpoxyController
import com.makentoshe.habrachan.ui.main.articles.ArticlesSearchFragmentUi
import toothpick.ktp.delegate.inject

class ArticlesSearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private val controller by inject<ArticlesSearchEpoxyController>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ArticlesSearchFragmentUi(container).buildView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById<RecyclerView>(R.id.articles_search_fragment_recycler)
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        recyclerView.adapter = controller.adapter
        controller.requestModelBuild()
    }

}

