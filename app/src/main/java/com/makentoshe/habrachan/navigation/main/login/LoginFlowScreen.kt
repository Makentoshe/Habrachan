package com.makentoshe.habrachan.navigation.main.login

import com.makentoshe.habrachan.navigation.Screen
import com.makentoshe.habrachan.view.main.login.LoginFlowFragment

class LoginFlowScreen : Screen() {
    override fun getFragment() = LoginFlowFragment.Factory().build()
}