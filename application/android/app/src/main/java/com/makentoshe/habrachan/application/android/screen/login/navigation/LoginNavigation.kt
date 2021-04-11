package com.makentoshe.habrachan.application.android.screen.login.navigation

import com.makentoshe.habrachan.application.android.navigation.StackRouter

class LoginNavigation(private val router: StackRouter) {

    fun toUserScreen() {
        TODO("Navigate to user screen")
    }

    fun back() {
        router.exit()
    }
}