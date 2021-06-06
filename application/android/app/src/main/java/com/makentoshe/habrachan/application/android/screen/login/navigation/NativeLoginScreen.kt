package com.makentoshe.habrachan.application.android.screen.login.navigation

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.screen.login.NativeLoginFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class NativeLoginScreen : SupportAppScreen() {
    override fun getFragment(): Fragment {
        return NativeLoginFragment.build()
    }
}