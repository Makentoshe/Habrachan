package com.makentoshe.habrachan.application.android.screen.main

import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class MainFlowScreen : SupportAppScreen() {
    override fun getFragment(): Fragment? {
        return MainFlowFragment()
    }
}