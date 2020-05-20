package com.makentoshe.habrachan.view.main

import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.broadcast.LoginBroadcastReceiver
import com.makentoshe.habrachan.common.broadcast.LogoutBroadcastReceiver
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.model.main.articles.ArticlesFlowScreen
import com.makentoshe.habrachan.model.main.login.LoginFlowScreen
import com.makentoshe.habrachan.model.main.menu.MenuScreen
import com.makentoshe.habrachan.ui.main.MainFlowFragmentUi
import com.makentoshe.habrachan.view.OnBackPressedFragment
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.NavigatorHolder
import toothpick.ktp.delegate.inject

class MainFlowFragment : Fragment(), OnBackPressedFragment {

    private val disposables = CompositeDisposable()
    private val arguments = Arguments(this)

    private val navigator by inject<Navigator>()
    private val sessionDatabase by inject<SessionDatabase>()
    private val logoutBroadcastReceiver by inject<LogoutBroadcastReceiver>()
    private val loginBroadcastReceiver by inject<LoginBroadcastReceiver>()

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
        if (sessionDatabase.me().isEmpty) setEmptyAccount(item) else setLoggedAccount(item)

        logoutBroadcastReceiver.observable.subscribe {
            setEmptyAccount(item)
        }.let(disposables::add)

        loginBroadcastReceiver.observable.subscribe {
            setLoggedAccountAlt(item, it)
        }.let(disposables::add)
    }

    private fun onNavigationItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_account -> onAccountClick()
        R.id.action_posts -> onArticlesClick()
        R.id.action_menu -> onMenuClick()
        else -> false
    }

    private fun onAccountClick(): Boolean {
        navigator.toLoginFlowScreen()
        return true
    }

    private fun onArticlesClick(): Boolean {
        navigator.toArticlesScreen(arguments.page)
        return true
    }

    private fun onMenuClick(): Boolean {
        navigator.toMenuScreen()
        return true
    }

    private fun setEmptyAccount(item: MenuItem) {
        item.setTitle(R.string.menu_account)
        item.setIcon(R.drawable.ic_account_outline)
    }

    private fun setLoggedAccount(item: MenuItem) {
        setLoggedAccountAlt(item, sessionDatabase.me().get().login)
    }

    private fun setLoggedAccountAlt(item: MenuItem, login: String) {
        item.title = login
        item.setIcon(R.drawable.ic_account)
    }

    override fun onStart() {
        super.onStart()
        navigator.init()
        val logoutIntentFilter = IntentFilter(LogoutBroadcastReceiver.ACTION)
        requireContext().registerReceiver(logoutBroadcastReceiver, logoutIntentFilter)
        val loginIntentFilter = IntentFilter(LoginBroadcastReceiver.ACTION)
        requireContext().registerReceiver(loginBroadcastReceiver, loginIntentFilter)
    }

    override fun onStop() {
        super.onStop()
        requireContext().unregisterReceiver(logoutBroadcastReceiver)
        requireContext().unregisterReceiver(loginBroadcastReceiver)
        navigator.release()
    }

    override fun onBackPressed(): Boolean {
        Log.i(javaClass.simpleName, "On Back Press Event")
        if (childFragmentManager.fragments.count() > 1) {
            navigator.back()
            return true
        }
        return false
    }

    class Navigator(
        private val router: Router,
        private val navigatorHolder: NavigatorHolder,
        private val navigator: com.makentoshe.habrachan.common.navigation.Navigator
    ) {

        fun init() {
            navigatorHolder.setNavigator(navigator)
        }

        fun toLoginFlowScreen() {
            router.forwardOrReplace(LoginFlowScreen())
        }

        fun toArticlesScreen(page: Int) {
            router.forwardOrReplace(ArticlesFlowScreen(page))
        }

        fun toMenuScreen() {
            router.forwardOrReplace(MenuScreen())
        }

        fun back() = router.softExit()

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


