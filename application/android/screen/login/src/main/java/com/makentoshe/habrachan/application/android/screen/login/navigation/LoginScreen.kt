package com.makentoshe.habrachan.application.android.screen.login.navigation

import com.makentoshe.habrachan.application.android.screen.login.LoginFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class LoginScreen : SupportAppScreen() {
    override fun getFragment() = LoginFragment().apply {

    }
}
