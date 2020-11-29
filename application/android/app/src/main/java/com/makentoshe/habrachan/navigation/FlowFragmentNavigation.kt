package com.makentoshe.habrachan.navigation

import androidx.lifecycle.LifecycleObserver
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder

abstract class FlowFragmentNavigation(
    private val navigatorHolder: NavigatorHolder,
    private val navigator: Navigator
) : LifecycleObserver {

    fun release() {
        navigatorHolder.removeNavigator()
    }

    fun init() {
        navigatorHolder.setNavigator(navigator)
    }
}