package com.makentoshe.habrachan.view.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.di.main.MainFlowFragmentModule
import com.makentoshe.habrachan.di.main.MainFlowFragmentScope
import com.makentoshe.habrachan.model.main.account.AccountFlowScreen
import com.makentoshe.habrachan.model.main.menu.MenuScreen
import com.makentoshe.habrachan.model.main.posts.ArticlesFlowScreen
import com.makentoshe.habrachan.ui.main.MainFlowFragmentUi
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import toothpick.smoothie.lifecycle.closeOnDestroy

class MainFlowFragment : Fragment() {

    private val arguments = Arguments(this)
    private val navigator by inject<MainFlowFragment.Navigator>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDependencies()
    }

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
        navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_account -> navigator.toAccountScreen()
                R.id.action_posts -> navigator.toArticlesScreen(arguments.page)
                R.id.action_menu -> navigator.toMenuScreen()
                else -> return@setOnNavigationItemSelectedListener false
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun onStart() {
        super.onStart()
        navigator.init()
    }

    override fun onStop() {
        super.onStop()
        navigator.release()
    }

    private fun injectDependencies() {
        val module = MainFlowFragmentModule(this)
        val scope = Toothpick.openScopes(ApplicationScope::class.java, MainFlowFragmentScope::class.java)
        scope.installModules(module).closeOnDestroy(this).inject(this)
    }

    class Navigator(
        private val router: Router,
        private val navigatorHolder: NavigatorHolder,
        private val navigator: com.makentoshe.habrachan.common.navigation.Navigator
    ) {

        fun init() {
            navigatorHolder.setNavigator(navigator)
        }

        fun toAccountScreen() {
            router.replaceScreen(AccountFlowScreen())
        }

        fun toArticlesScreen(page: Int) {
            router.replaceScreen(ArticlesFlowScreen(page))
        }

        fun toMenuScreen() {
            router.replaceScreen(MenuScreen())
        }

        fun release() {
            navigatorHolder.removeNavigator()
        }
    }

    class Arguments(fragment: MainFlowFragment) {

        init {
            (fragment as Fragment).arguments = Bundle()
        }

        private val fragmentArguments = fragment.requireArguments()

        var page: Int
            get() = fragmentArguments.getInt(PAGE, 1)
            set(value) = fragmentArguments.putInt(PAGE, value)

        companion object {
            private const val PAGE = "Page"
        }

    }
}


