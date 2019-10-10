package com.makentoshe.habrachan.model.menu

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.navigation.Screen
import com.makentoshe.habrachan.view.menu.MenuFragment

class MenuScreen : Screen() {
    override val fragment: Fragment
        get() = MenuFragment()
}