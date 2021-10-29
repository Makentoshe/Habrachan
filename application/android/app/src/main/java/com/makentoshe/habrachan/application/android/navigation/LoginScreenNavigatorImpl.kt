package com.makentoshe.habrachan.application.android.navigation

import com.makentoshe.habrachan.application.android.screen.articles.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.LoginScreenNavigator
import com.makentoshe.habrachan.application.android.screen.login.navigation.LoginScreen
import javax.inject.Inject

class LoginScreenNavigatorImpl @Inject constructor(
    private val router: StackRouter,
) : LoginScreenNavigator {
    override fun toLoginScreen() {
        router.navigateTo(LoginScreen())
    }
}