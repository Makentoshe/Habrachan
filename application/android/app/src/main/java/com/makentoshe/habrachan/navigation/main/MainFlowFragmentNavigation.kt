package com.makentoshe.habrachan.navigation.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.navigation.main.articles.ArticlesFlowScreen
import com.makentoshe.habrachan.navigation.main.login.LoginFlowScreen
import com.makentoshe.habrachan.navigation.main.menu.MenuScreen
import ru.terrakok.cicerone.android.support.SupportAppScreen

class MainFlowFragmentNavigation(private val fragmentManager: FragmentManager) {

    // TODO add stack for navigating back
    private var currentScreen: SupportAppScreen? = null

    fun navigateToArticlesScreen(page: Int) {
        val newScreen = ArticlesFlowScreen(page)
        navigateToScreen(newScreen)
    }

    fun navigateToAccountScreen() {
        val newScreen = LoginFlowScreen()
        navigateToScreen(newScreen)
    }

    fun navigateToMenuScreen() {
        val newScreen = MenuScreen()
        navigateToScreen(newScreen)
    }

    private fun navigateToScreen(newScreen: SupportAppScreen) {
        // if screen already displaying
        if (currentScreen?.screenKey == newScreen.screenKey) return

        val currentFragment = fragmentManager.findFragmentByTag(currentScreen?.screenKey)
        val newFragment = fragmentManager.findFragmentByTag(newScreen.screenKey)
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
        val transaction = fragmentManager.beginTransaction()
        if (oldFragment != null) transaction.hide(oldFragment)
        transaction.show(newFragment)
        transaction.commitNow()
    }

    private fun addScreen(newScreen: SupportAppScreen, oldFragment: Fragment?) {
        val transaction = fragmentManager.beginTransaction()
        if (oldFragment != null) transaction.hide(oldFragment)
        transaction.add(R.id.fragment_main_container, newScreen.fragment!!, newScreen.screenKey)
        transaction.commitNow()
    }

}