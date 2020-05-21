package com.makentoshe.habrachan.navigation.main.login

import com.makentoshe.habrachan.navigation.Screen
import com.makentoshe.habrachan.view.main.login.LoginFragment

class LoginScreen : Screen() {
    override fun getFragment() = LoginFragment.Factory().build()
}