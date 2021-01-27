package com.makentoshe.habrachan.application.android.screen.main.navigation

import com.makentoshe.habrachan.application.android.screen.main.MainFlowFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class MainFlowScreen : SupportAppScreen() {
    override fun getFragment() = MainFlowFragment()
}
