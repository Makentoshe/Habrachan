package com.makentoshe.habrachan.model.main.account.login

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.navigation.Screen
import com.makentoshe.habrachan.view.main.account.login.LoginFragment

class LoginScreen : Screen() {
    override val fragment: Fragment
        get() = LoginFragment.Factory().build()
}