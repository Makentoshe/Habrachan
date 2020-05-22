package com.makentoshe.habrachan.navigation.main

import com.makentoshe.habrachan.navigation.Router
import com.makentoshe.habrachan.navigation.main.articles.ArticlesFlowScreen
import com.makentoshe.habrachan.navigation.main.login.LoginFlowScreen
import com.makentoshe.habrachan.navigation.main.menu.MenuScreen
import ru.terrakok.cicerone.NavigatorHolder

class MainFlowNavigator(
    private val router: Router,
    private val navigatorHolder: NavigatorHolder,
    private val navigator: ru.terrakok.cicerone.Navigator
) {

    fun init() {
        navigatorHolder.setNavigator(navigator)
    }

    fun toLoginFlowScreen() {
        router.replaceScreen(LoginFlowScreen())
    }

    fun toArticlesScreen(page: Int) {
        router.replaceScreen(ArticlesFlowScreen(page))
    }

    fun toMenuScreen() {
        router.replaceScreen(MenuScreen())
    }

    fun back() {
        router.exit()
    }

    fun release() {
        navigatorHolder.removeNavigator()
    }
}