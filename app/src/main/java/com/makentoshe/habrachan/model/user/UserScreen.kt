package com.makentoshe.habrachan.model.user

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.navigation.Screen
import com.makentoshe.habrachan.view.user.UserFragment

class UserScreen(private val userAccount: UserAccount) : Screen() {

    override val fragment: Fragment
        get() = UserFragment.Factory().build(userAccount)
}
