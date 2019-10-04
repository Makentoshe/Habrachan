package com.makentoshe.habrachan.model

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.model.navigation.Screen
import com.makentoshe.habrachan.view.MainFlowFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class MainFlowScreen : Screen() {
    override val fragment: Fragment
        get() = MainFlowFragment()
}