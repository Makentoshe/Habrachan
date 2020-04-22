package com.makentoshe.habrachan.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.model.main.articles.ArticlesFlowScreen
import com.makentoshe.habrachan.model.main.login.LoginScreen
import com.makentoshe.habrachan.model.main.menu.MenuScreen
import com.makentoshe.habrachan.model.user.UserAccount
import com.makentoshe.habrachan.model.user.UserScreen
import com.makentoshe.habrachan.ui.main.MainFlowFragmentUi
import ru.terrakok.cicerone.NavigatorHolder
import toothpick.ktp.delegate.inject

class MainFlowFragment : Fragment() {

    private val arguments = Arguments(this)
    private val navigator by inject<Navigator>()
    private val sessionDatabase by inject<SessionDatabase>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            navigator.toArticlesScreen(page = 1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return MainFlowFragmentUi().createView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val navigation = view.findViewById<BottomNavigationView>(R.id.main_bottom_navigation)
        navigation.setOnNavigationItemSelectedListener(::onNavigationItemSelected)

        val item = navigation.menu.findItem(R.id.action_account)
        if (!sessionDatabase.me().isEmpty) {
            item.title = sessionDatabase.me().get().login
            item.setIcon(R.drawable.ic_account)
        } else {
            item.setTitle(R.string.menu_account)
            item.setIcon(R.drawable.ic_account_outline)
        }
    }

    private fun onNavigationItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_account -> {
            onAccountClick(); true
        }
        R.id.action_posts -> {
            onArticlesClick(); true
        }
        R.id.action_menu -> {
            onMenuClick(); true
        }
        else -> false
    }

    private fun onAccountClick() {
        if (sessionDatabase.me().isEmpty) {
            navigator.toLoginScreen()
        } else {
            navigator.toUserScreen()
        }
    }

    private fun onArticlesClick() {
        navigator.toArticlesScreen(arguments.page)
    }

    private fun onMenuClick() {
        navigator.toMenuScreen()
    }

    override fun onStart() {
        super.onStart()
        navigator.init()
    }

    override fun onStop() {
        super.onStop()
        navigator.release()
    }

    class Navigator(
        private val router: Router,
        private val navigatorHolder: NavigatorHolder,
        private val navigator: com.makentoshe.habrachan.common.navigation.Navigator
    ) {

        fun init() {
            navigatorHolder.setNavigator(navigator)
        }

        fun toUserScreen() {
            router.smartReplace(UserScreen(UserAccount.Me))
        }

        fun toLoginScreen() {
            router.smartReplace(LoginScreen())
        }

        fun toArticlesScreen(page: Int) {
            router.smartReplace(ArticlesFlowScreen(page))
        }

        fun toMenuScreen() {
            router.smartReplace(MenuScreen())
        }

        fun release() {
            navigatorHolder.removeNavigator()
        }
    }

    class Arguments(private val mainFlowFragment: MainFlowFragment) {

        init {
            val fragment = mainFlowFragment as Fragment
            if (fragment.arguments == null) {
                fragment.arguments = Bundle()
            }
        }

        private val fragmentArguments: Bundle
            get() = mainFlowFragment.requireArguments()

        var page: Int
            get() = fragmentArguments.getInt(PAGE, 1)
            set(value) = fragmentArguments.putInt(PAGE, value)

        companion object {
            private const val PAGE = "Page"
        }

    }
}


