package com.makentoshe.habrachan.application.android.screen.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.binding.attachBinding
import com.makentoshe.habrachan.application.android.common.fragment.BaseFragment
import com.makentoshe.habrachan.application.android.common.fragment.FragmentArguments
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionProvider
import com.makentoshe.habrachan.application.android.common.usersession.isUserLoggedIn
import com.makentoshe.habrachan.application.android.screen.articles.databinding.FragmentFlowArticlesBinding
import com.makentoshe.habrachan.application.android.screen.articles.di.provider.ArticlesFlowAdapterProvider
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesUserSearch
import com.makentoshe.habrachan.application.android.screen.articles.model.TabLayoutMediatorController
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.LoginScreenNavigator
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.MeScreenNavigator
import com.makentoshe.habrachan.network.request.SpecType
import kotlinx.android.synthetic.main.fragment_flow_articles.*
import toothpick.ktp.delegate.inject

class ArticlesFlowFragment : BaseFragment() {

    companion object : Analytics(LogAnalytic()) {
        fun build(articlesUserSearches: List<ArticlesUserSearch>) = ArticlesFlowFragment().apply {
            arguments.userSearchesCount = articlesUserSearches.size
        }
    }

    override val arguments = Arguments(this)
    private val binding: FragmentFlowArticlesBinding by attachBinding()

    private val meScreenNavigator by inject<MeScreenNavigator>()
    private val loginScreenNavigator by inject<LoginScreenNavigator>()
    private val androidUserSessionProvider by inject<AndroidUserSessionProvider>()
    private val tabLayoutMediatorController by inject<TabLayoutMediatorController>()
    private val adapterProvider by inject<ArticlesFlowAdapterProvider>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        capture(analyticEvent { "OnCreate($savedInstanceState)" })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_flow_articles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragment_flow_articles_viewpager.adapter = adapterProvider.get()
        tabLayoutMediatorController.attach(fragment_flow_articles_tabs, fragment_flow_articles_viewpager)

        if (androidUserSessionProvider.get()?.isUserLoggedIn == true) updateToolbarLogin() else updateToolbarLogout()
        binding.fragmentFlowArticlesToolbar.setNavigationOnClickListener {
//            Toast.makeText(requireContext(), R.string.not_implemented, Toast.LENGTH_LONG).show()
            navigateToUserOrLoginScreen()
        }
    }

    private fun updateToolbarLogout() {
        binding.fragmentFlowArticlesToolbar.setNavigationIcon(R.drawable.ic_account_outline)
        binding.fragmentFlowArticlesToolbar.tag = R.drawable.ic_account_outline
        binding.fragmentFlowArticlesToolbar.setTitle(R.string.app_name)
    }

    private fun updateToolbarLogin() {
        binding.fragmentFlowArticlesToolbar.setNavigationIcon(R.drawable.ic_account)
        binding.fragmentFlowArticlesToolbar.tag = R.drawable.ic_account
        binding.fragmentFlowArticlesToolbar.setTitle(R.string.app_name)

//        binding.fragmentFlowArticlesToolbar.subtitle = userSession.user?.login
    }

    private fun navigateToUserOrLoginScreen() = if (androidUserSessionProvider.get()?.isUserLoggedIn == true) {
        meScreenNavigator.toMeScreen()
    } else {
        loginScreenNavigator.toLoginScreen()
    }

    class Arguments(fragment: ArticlesFlowFragment) : FragmentArguments(fragment) {

        @Suppress("UNCHECKED_CAST")
        var specs: List<SpecType>
            get() = fragmentArguments.get(SPECS) as? ArrayList<SpecType> ?: emptyList()
            set(value) = fragmentArguments.putSerializable(SPECS, ArrayList(value))

        var userSearchesCount: Int
            get() = fragmentArguments.getInt(USER_SEARCHES)
            set(value) = fragmentArguments.putInt(USER_SEARCHES, value)

        companion object {
            private const val SPECS = "Specs"
            private const val USER_SEARCHES = "UserSearches"
        }
    }
}
