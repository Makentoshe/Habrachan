package com.makentoshe.habrachan.navigation.main

import com.makentoshe.habrachan.navigation.FlowFragmentNavigation
import com.makentoshe.habrachan.navigation.main.articles.ArticlesFlowScreen
import com.makentoshe.habrachan.navigation.main.login.LoginFlowScreen
import com.makentoshe.habrachan.navigation.main.menu.MenuScreen
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

class MainFlowFragmentNavigation(
    private val router: Router, navigatorHolder: NavigatorHolder, navigator: ru.terrakok.cicerone.Navigator
) : FlowFragmentNavigation(navigatorHolder, navigator) {

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
}