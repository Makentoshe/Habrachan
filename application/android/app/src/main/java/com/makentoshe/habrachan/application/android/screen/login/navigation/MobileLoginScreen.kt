package com.makentoshe.habrachan.application.android.screen.login.navigation

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.screen.login.MobileLoginFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class MobileLoginScreen : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return MobileLoginFragment.build()
    }
}