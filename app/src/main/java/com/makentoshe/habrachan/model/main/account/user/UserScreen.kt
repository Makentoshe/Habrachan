package com.makentoshe.habrachan.model.main.account.user

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.navigation.Screen
import com.makentoshe.habrachan.view.main.account.user.UserFragment

class UserScreen(private val userName: String?) : Screen() {
    override val fragment: Fragment
        get() = UserFragment.Factory().run { if (userName == null) build() else build(userName) }
}