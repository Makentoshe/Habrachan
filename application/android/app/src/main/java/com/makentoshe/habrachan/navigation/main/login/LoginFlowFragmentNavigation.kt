package com.makentoshe.habrachan.navigation.main.login

import com.makentoshe.habrachan.model.user.UserAccount
import com.makentoshe.habrachan.navigation.FlowFragmentNavigation
import com.makentoshe.habrachan.navigation.user.UserScreen
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

class LoginFlowFragmentNavigation(
    private val router: Router, navigatorHolder: NavigatorHolder, navigator: ru.terrakok.cicerone.Navigator
) : FlowFragmentNavigation(navigatorHolder, navigator) {

    fun toLoginScreen() {
        router.replaceScreen(LoginScreen())
    }

    fun toUserScreen(userAccount: UserAccount) {
        router.replaceScreen(UserScreen(userAccount))
    }
}
