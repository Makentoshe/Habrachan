package com.makentoshe.habrachan.common.navigation.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.makentoshe.habrachan.common.navigation.Navigator
import ru.terrakok.cicerone.NavigatorHolder

class NavigatorHolderLifecycleObserver(
    private val navigatorHolder: NavigatorHolder,
    private val navigator: Navigator
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun setNavigator() {
        navigatorHolder.setNavigator(navigator)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun removeNavigator() {
        navigatorHolder.removeNavigator()
    }
}