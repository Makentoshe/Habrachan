package com.makentoshe.habrachan.navigation.user

import com.makentoshe.habrachan.model.user.UserAccount
import com.makentoshe.habrachan.navigation.Screen
import com.makentoshe.habrachan.view.user.UserFragment

class UserScreen(private val userAccount: UserAccount) : Screen() {
    override fun getFragment() = UserFragment.Factory().build(userAccount)
}
