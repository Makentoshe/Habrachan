package com.makentoshe.habrachan.application.android.screen.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.AndroidUserSession
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.broadcast.ApplicationState
import com.makentoshe.habrachan.application.android.broadcast.ApplicationStateBroadcastReceiver
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesFlowAdapter
import com.makentoshe.habrachan.application.android.screen.articles.navigation.ArticlesNavigation
import com.makentoshe.habrachan.network.request.SpecType
import com.makentoshe.habrachan.network.request.TopSpecType
import kotlinx.android.synthetic.main.fragment_flow_articles.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
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
    private val androidUserSession by inject<AndroidUserSession>()
    private val applicationStateBroadcastReceiver by inject<ApplicationStateBroadcastReceiver>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_flow_articles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragment_flow_articles_viewpager.adapter = adapter

        TabLayoutMediator(fragment_flow_articles_tabs, fragment_flow_articles_viewpager) { tab, position ->
            tab.text = arguments.specs[position].title()
        }.attach()

        if (androidUserSession.isLoggedIn) {
            fragment_flow_articles_toolbar.setNavigationIcon(R.drawable.ic_account)
        } else {
            fragment_flow_articles_toolbar.setNavigationIcon(R.drawable.ic_account_outline)
        }

        fragment_flow_articles_toolbar.setNavigationOnClickListener {
            if (androidUserSession.isLoggedIn) navigation.navigateToUser() else navigation.navigateToLogin()
        }

        lifecycleScope.launch {
            applicationStateBroadcastReceiver.applicationStateChannel.receiveAsFlow().collect { state ->
                when (state) {
                    ApplicationState.SignOut -> {
                        fragment_flow_articles_toolbar.setNavigationIcon(R.drawable.ic_account_outline)
                    }
                    ApplicationState.SignIn -> {
                        fragment_flow_articles_toolbar.setNavigationIcon(R.drawable.ic_account)
                    }
                }
            }
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
