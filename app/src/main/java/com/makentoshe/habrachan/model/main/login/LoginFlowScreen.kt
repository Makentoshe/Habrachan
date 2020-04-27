package com.makentoshe.habrachan.model.main.login

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.navigation.Screen
import com.makentoshe.habrachan.view.main.login.LoginFlowFragment

class LoginFlowScreen : Screen() {
    override val fragment: Fragment
        get() = LoginFlowFragment.Factory().build()
}