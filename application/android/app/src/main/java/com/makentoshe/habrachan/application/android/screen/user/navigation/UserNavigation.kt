package com.makentoshe.habrachan.application.android.screen.user.navigation

import com.makentoshe.habrachan.application.android.navigation.StackRouter

class UserNavigation(private val stackRouter: StackRouter) {

    fun back() = stackRouter.exit()
}