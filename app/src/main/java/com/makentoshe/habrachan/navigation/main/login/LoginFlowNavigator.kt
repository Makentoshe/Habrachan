package com.makentoshe.habrachan.navigation.main.login

import com.makentoshe.habrachan.model.user.UserAccount
import com.makentoshe.habrachan.navigation.Router
import com.makentoshe.habrachan.navigation.main.login.LoginScreen
import com.makentoshe.habrachan.navigation.user.UserScreen
import ru.terrakok.cicerone.NavigatorHolder

class LoginFlowNavigator(
    private val router: Router,
    private val navigatorHolder: NavigatorHolder,
    private val navigator: ru.terrakok.cicerone.Navigator
) {

    fun init() {
        navigatorHolder.setNavigator(navigator)
    }

    fun toLoginScreen() {
        router.replaceScreen(LoginScreen())
    }

    fun toUserScreen(userAccount: UserAccount) {
        router.replaceScreen(UserScreen(userAccount))
    }

    fun release() {
        navigatorHolder.removeNavigator()
    }
}