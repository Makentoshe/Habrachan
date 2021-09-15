package com.makentoshe.habrachan.application.android.navigation

import com.makentoshe.habrachan.application.android.navigation.navigator.LoginScreenNavigator
import com.makentoshe.habrachan.application.android.screen.login.navigation.LoginScreen
import javax.inject.Inject

class LoginScreenNavigatorImpl @Inject constructor(
    private val router: StackRouter,
) : LoginScreenNavigator {
    override fun toLoginScreen(shouldNavigateToUserScreenAfterLogin: Boolean) {
        router.navigateTo(LoginScreen(shouldNavigateToUserScreenAfterLogin))
    }
}