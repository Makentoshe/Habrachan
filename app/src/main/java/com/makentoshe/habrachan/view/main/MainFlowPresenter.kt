package com.makentoshe.habrachan.view.main

import com.makentoshe.habrachan.common.navigation.Navigator
import com.makentoshe.habrachan.model.main.account.AccountScreen
import com.makentoshe.habrachan.model.main.menu.MenuScreen
import com.makentoshe.habrachan.model.main.posts.PostsScreen
import com.makentoshe.habrachan.viewmodel.main.MainFlowViewModel

class MainFlowPresenter(
    private val viewModel: MainFlowViewModel,
    private val navigator: Navigator
) {

    fun setDefaultScreen() {
        onPostsClicked()
    }

    fun initNavigator() {
        viewModel.navigatorHolder.setNavigator(navigator)
    }

    fun releaseNavigator() {
        viewModel.navigatorHolder.removeNavigator()
    }

    fun onAccountClicked(): Boolean {
        viewModel.router.replaceScreen(AccountScreen())
        return true
    }

    fun onPostsClicked(): Boolean {
        val screen = PostsScreen(viewModel.page)
        viewModel.router.replaceScreen(screen)
        return true
    }

    fun onMenuClicked(): Boolean {
        val screen = MenuScreen()
        viewModel.router.replaceScreen(MenuScreen())
        return true
    }
}