package com.makentoshe.habrachan.application.android.screen.main.account

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class AccountScreen(): SupportAppScreen() {
    override fun getFragment(): Fragment? {
        return AccountFragment.build()
    }
}