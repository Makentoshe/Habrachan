package com.makentoshe.habrachan.application.android.screen.login.navigation

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.screen.login.LoginFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class LoginScreen(
    private val shouldNavigateToUserScreenAfterLogin: Boolean = false
) : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return LoginFragment.build(shouldNavigateToUserScreenAfterLogin)
    }
}