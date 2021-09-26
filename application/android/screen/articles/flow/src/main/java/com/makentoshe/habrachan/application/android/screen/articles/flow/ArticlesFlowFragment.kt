package com.makentoshe.habrachan.application.android.screen.articles.flow

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.AndroidUserSession2
import com.makentoshe.habrachan.application.android.common.fragment.BaseFragment
import com.makentoshe.habrachan.application.android.common.fragment.FragmentArguments
import com.makentoshe.habrachan.application.android.screen.articles.flow.model.ArticlesFlowAdapter
import com.makentoshe.habrachan.application.android.screen.articles.flow.model.TabLayoutMediatorController
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.LoginScreenNavigator
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.MeScreenNavigator
import com.makentoshe.habrachan.network.request.SpecType
import kotlinx.android.synthetic.main.fragment_flow_articles.*
import toothpick.ktp.delegate.inject

class ArticlesFlowFragment : BaseFragment() {

    companion object : Analytics(LogAnalytic()) {
        fun build(specs: List<SpecType>) = ArticlesFlowFragment().apply {
            arguments.specs = specs
        }
    }

    override val arguments = Arguments(this)

    private val adapter by inject<ArticlesFlowAdapter>()
    private val meScreenNavigator by inject<MeScreenNavigator>()
    private val loginScreenNavigator by inject<LoginScreenNavigator>()
    private val userSession by inject<AndroidUserSession2>()
    private val tabLayoutMediatorController by inject<TabLayoutMediatorController>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        capture(analyticEvent("") { "${this@ArticlesFlowFragment} Attach(${arguments.specs})" })
    }

    override fun internalOnCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View {
        return inflater.inflate(R.layout.fragment_flow_articles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        capture(analyticEvent("") { "${this@ArticlesFlowFragment} OnViewCreated($savedInstanceState)" })
        fragment_flow_articles_viewpager.adapter = adapter
        tabLayoutMediatorController.attach(fragment_flow_articles_tabs, fragment_flow_articles_viewpager)

        if (userSession.isLoggedIn) updateToolbarLogin() else updateToolbarLogout()
        fragment_flow_articles_toolbar.setNavigationOnClickListener {
            Toast.makeText(requireContext(), R.string.not_implemented, Toast.LENGTH_LONG).show()
//            if (userSession.isLoggedIn) meScreenNavigator.toMeScreen() else loginScreenNavigator.toLoginScreen()
        }
    }

    private fun updateToolbarLogout() {
        fragment_flow_articles_toolbar.setNavigationIcon(R.drawable.ic_account_outline)
        fragment_flow_articles_toolbar.tag = R.drawable.ic_account_outline
        fragment_flow_articles_toolbar.setTitle(R.string.app_name)
    }

    private fun updateToolbarLogin() {
        fragment_flow_articles_toolbar.setNavigationIcon(R.drawable.ic_account)
        fragment_flow_articles_toolbar.tag = R.drawable.ic_account
        fragment_flow_articles_toolbar.setTitle(R.string.app_name)
        fragment_flow_articles_toolbar.subtitle = userSession.user?.login
    }

    class Arguments(fragment: ArticlesFlowFragment) : FragmentArguments(fragment) {

        var specs: List<SpecType>
            get() = fragmentArguments.get(SPECS) as? ArrayList<SpecType> ?: emptyList()
            set(value) = fragmentArguments.putSerializable(SPECS, ArrayList(value))

        companion object {
            private const val SPECS = "Specs"
        }
    }
}
