package com.makentoshe.habrachan.application.android.screen.login.navigation

import com.makentoshe.habrachan.application.android.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.user.navigation.UserScreen

class LoginNavigation(private val router: StackRouter) {

    fun toUserScreen() {
        router.exit()
        router.stack(UserScreen())
    }

    fun back() {
        router.exit()
    }
}