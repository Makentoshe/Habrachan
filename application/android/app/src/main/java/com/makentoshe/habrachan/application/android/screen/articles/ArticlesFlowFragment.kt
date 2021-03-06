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

        if (androidUserSession.isLoggedIn) updateToolbarLogin() else updateToolbarLogout()
        fragment_flow_articles_toolbar.setNavigationOnClickListener {
            if (androidUserSession.isLoggedIn) navigation.navigateToUser() else navigation.navigateToLogin()
        }

        lifecycleScope.launch {
            applicationStateBroadcastReceiver.applicationStateChannel.receiveAsFlow().collect { state ->
                when (state) {
                    ApplicationState.SignOut -> updateToolbarLogout()
                    ApplicationState.SignIn -> updateToolbarLogin()
                }
            }
        }
    }

    private fun updateToolbarLogout() {
        fragment_flow_articles_toolbar.setNavigationIcon(R.drawable.ic_account_outline)
        fragment_flow_articles_toolbar.setTitle(R.string.app_name)
    }

    private fun updateToolbarLogin() {
        fragment_flow_articles_toolbar.setNavigationIcon(R.drawable.ic_account)
        fragment_flow_articles_toolbar.title = androidUserSession.user?.login ?: getString(R.string.app_name)
    }

    private fun SpecType.title(): String = when (this) {
        is SpecType.All -> getString(R.string.articles_type_all)
        is SpecType.Interesting -> getString(R.string.articles_type_interesting)
        is SpecType.Top -> when (this.type) {
            is TopSpecType.Alltime -> getString(R.string.articles_top_type_alltime)
            is TopSpecType.Yearly -> getString(R.string.articles_top_type_yearly)
            is TopSpecType.Monthly -> getString(R.string.articles_top_type_monthly)
            is TopSpecType.Weekly -> getString(R.string.articles_top_type_weekly)
            is TopSpecType.Daily -> getString(R.string.articles_top_type_daily)
            else -> getString(R.string.not_implemented)
        }.let { getString(R.string.articles_top_preposition, it) }
        else -> getString(R.string.not_implemented)
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
