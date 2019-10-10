package com.makentoshe.habrachan.model

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.navigation.Screen
import com.makentoshe.habrachan.view.MainFlowFragment

class MainFlowScreen : Screen() {
    override val fragment: Fragment
        get() = MainFlowFragment()
}