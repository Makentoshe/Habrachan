package com.makentoshe.habrachan.application.android.screen.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesFlowAdapter
import com.makentoshe.habrachan.network.request.GetArticlesRequest
import kotlinx.android.synthetic.main.fragment_flow_articles.*
import toothpick.ktp.delegate.inject

class ArticlesFlowFragment : CoreFragment() {

    companion object {
        fun build(specs: List<GetArticlesRequest.Spec>) = ArticlesFlowFragment().apply {
            arguments.specs = specs
        }
    }

    override val arguments = Arguments(this)
    private val adapter by inject<ArticlesFlowAdapter>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_flow_articles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragment_flow_articles_viewpager.adapter = adapter

        TabLayoutMediator(fragment_flow_articles_tabs, fragment_flow_articles_viewpager) { tab, position ->
            tab.text = arguments.specs[position].title()
        }.attach()
    }

    private fun GetArticlesRequest.Spec.title(): String = when (this) {
        is GetArticlesRequest.Spec.All -> getString(R.string.articles_type_all)
        is GetArticlesRequest.Spec.Interesting -> getString(R.string.articles_type_interesting)
        is GetArticlesRequest.Spec.Top -> when (this.type) {
            GetArticlesRequest.Spec.Top.Type.AllTime -> getString(R.string.articles_top_type_alltime)
            GetArticlesRequest.Spec.Top.Type.Yearly -> getString(R.string.articles_top_type_yearly)
            GetArticlesRequest.Spec.Top.Type.Monthly -> getString(R.string.articles_top_type_monthly)
            GetArticlesRequest.Spec.Top.Type.Weekly -> getString(R.string.articles_top_type_weekly)
            GetArticlesRequest.Spec.Top.Type.Daily -> getString(R.string.articles_top_type_daily)
        }.let { getString(R.string.articles_top_preposition, it) }
    }

    class Arguments(fragment: ArticlesFlowFragment) : CoreFragment.Arguments(fragment) {

        var specs: List<GetArticlesRequest.Spec>
            get() = fragmentArguments.getStringArrayList(SPECS)?.mapNotNull { GetArticlesRequest.Spec.build(it) }
                ?: emptyList()
            set(value) = fragmentArguments.putStringArrayList(SPECS, ArrayList(value.map { it.request }))

        companion object {
            private const val SPECS = "Specs"
        }
    }
}
