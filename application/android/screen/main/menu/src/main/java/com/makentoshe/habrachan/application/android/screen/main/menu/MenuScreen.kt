package com.makentoshe.habrachan.application.android.screen.main.menu

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class MenuScreen : SupportAppScreen() {
    override fun getFragment(): Fragment? {
        return MenuFragment()
    }
}