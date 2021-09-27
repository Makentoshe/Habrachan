package com.makentoshe.habrachan.application.android.screen.articles.navigation

import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.UserScreenNavigator
import com.makentoshe.habrachan.application.android.screen.user.model.UserAccount
import com.makentoshe.habrachan.application.android.screen.user.navigation.UserScreen
import javax.inject.Inject

class UserScreenNavigatorImpl @Inject constructor(private val router: StackRouter): UserScreenNavigator {
    override fun toUserScreen(login: String) {
        router.stack(UserScreen(UserAccount.User(login)))
    }
}