package com.makentoshe.habrachan.view

import com.makentoshe.habrachan.common.model.navigation.Navigator
import com.makentoshe.habrachan.model.account.AccountScreen
import com.makentoshe.habrachan.model.posts.PostsScreen
import com.makentoshe.habrachan.viewmodel.MainFlowViewModel

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
}