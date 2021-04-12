package com.makentoshe.habrachan.application.android.screen.login.navigation

import com.makentoshe.habrachan.application.android.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.user.navigation.UserScreen
import com.makentoshe.habrachan.application.android.screen.user.model.UserAccount
import com.makentoshe.habrachan.entity.User

class LoginNavigation(private val router: StackRouter) {

    fun toUserScreen(user: User?) {
        router.exit()
        router.stack(UserScreen(UserAccount.Me(user)))
    }

    fun back() {
        router.exit()
    }
}