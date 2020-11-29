package com.makentoshe.habrachan.application.android.screen.main.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.makentoshe.habrachan.application.android.main.R
import com.makentoshe.habrachan.application.android.screen.main.AccountScreen
import com.makentoshe.habrachan.application.android.screen.main.MenuScreen
import com.makentoshe.habrachan.application.android.screen.main.articles.ArticlesScreen
import ru.terrakok.cicerone.android.support.SupportAppScreen

class MainFlowNavigationImpl(private val childFragmentManager: FragmentManager): MainFlowNavigation {

    // TODO add stack for navigating back
    private var currentScreen: SupportAppScreen? = null

    override fun navigateToArticlesScreen() {
        val newScreen = ArticlesScreen()
        navigateToScreen(newScreen)
    }

    override fun navigateToAccountScreen() {
        val newScreen = AccountScreen()
        navigateToScreen(newScreen)
    }

    override fun navigateToMenuScreen() {
        val newScreen = MenuScreen()
        navigateToScreen(newScreen)
    }

    private fun navigateToScreen(newScreen: SupportAppScreen) {
        // if screen already displaying
        if (currentScreen?.screenKey == newScreen.screenKey) return

        val currentFragment = childFragmentManager.findFragmentByTag(currentScreen?.screenKey)
        val newFragment = childFragmentManager.findFragmentByTag(newScreen.screenKey)
        if (newFragment != null) {
            // if contains fragment in backstack - show it
            replaceScreen(newFragment, currentFragment)
        } else {
            // if currently shows another fragment - hide it
            addScreen(newScreen, currentFragment)
        }

        currentScreen = newScreen
    }

    private fun replaceScreen(newFragment: Fragment, oldFragment: Fragment?) {
        val transaction = childFragmentManager.beginTransaction()
        if (oldFragment != null) transaction.hide(oldFragment)
        transaction.show(newFragment)
        transaction.commitNow()
    }

    private fun addScreen(newScreen: SupportAppScreen, oldFragment: Fragment?) {
        val transaction = childFragmentManager.beginTransaction()
        if (oldFragment != null) transaction.hide(oldFragment)
        transaction.add(R.id.fragment_main_container, newScreen.fragment!!, newScreen.screenKey)
        transaction.commitNow()
    }
}