package com.makentoshe.habrachan.application.android.screen.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.tabs.TabLayoutMediator
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesFlowAdapter
import com.makentoshe.habrachan.application.android.screen.articles.navigation.ArticlesNavigation
import com.makentoshe.habrachan.network.request.SpecType
import com.makentoshe.habrachan.network.request.TopSpecType
import kotlinx.android.synthetic.main.fragment_flow_articles.*
import toothpick.ktp.delegate.inject

class ArticlesFlowFragment : CoreFragment() {

    companion object {
        fun build(specs: List<SpecType>) = ArticlesFlowFragment().apply {
            arguments.specs = specs
        }
    }

    override val arguments = Arguments(this)

    private val adapter by inject<ArticlesFlowAdapter>()
    private val navigation by inject<ArticlesNavigation>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_flow_articles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragment_flow_articles_viewpager.adapter = adapter

        TabLayoutMediator(fragment_flow_articles_tabs, fragment_flow_articles_viewpager) { tab, position ->
            tab.text = arguments.specs[position].title()
        }.attach()

        fragment_flow_articles_toolbar.setNavigationOnClickListener {
            navigation.navigateToLogin()
        }
    }

    private fun SpecType.title(): String = when (this) {
        is SpecType.All -> getString(R.string.articles_type_all)
        is SpecType.Interesting -> getString(R.string.articles_type_interesting)
        is SpecType.Top -> when (this.type) {
            TopSpecType.Alltime -> getString(R.string.articles_top_type_alltime)
            TopSpecType.Yearly -> getString(R.string.articles_top_type_yearly)
            TopSpecType.Monthly -> getString(R.string.articles_top_type_monthly)
            TopSpecType.Weekly -> getString(R.string.articles_top_type_weekly)
            TopSpecType.Daily -> getString(R.string.articles_top_type_daily)
            else -> ""
        }.let { getString(R.string.articles_top_preposition, it) }
        else -> ""
    }

    class Arguments(fragment: ArticlesFlowFragment) : CoreFragment.Arguments(fragment) {

        var specs: List<SpecType>
            get() = fragmentArguments.get(SPECS) as? ArrayList<SpecType> ?: emptyList()
            set(value) = fragmentArguments.putSerializable(SPECS, ArrayList(value))

        companion object {
            private const val SPECS = "Specs"
        }
    }
}
