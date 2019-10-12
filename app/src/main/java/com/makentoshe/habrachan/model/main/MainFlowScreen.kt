package com.makentoshe.habrachan.model.main

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.navigation.Screen
import com.makentoshe.habrachan.view.main.MainFlowFragment

class MainFlowScreen : Screen() {
    override val fragment: Fragment
        get() = MainFlowFragment()
}