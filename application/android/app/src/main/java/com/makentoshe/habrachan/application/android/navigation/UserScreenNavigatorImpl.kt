package com.makentoshe.habrachan.application.android.navigation

import com.makentoshe.habrachan.application.android.screen.articles.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.UserScreenNavigator
import javax.inject.Inject

class UserScreenNavigatorImpl @Inject constructor(private val router: StackRouter): UserScreenNavigator {
    override fun toUserScreen(login: String) {
//        router.stack(
//            com.makentoshe.habrachan.application.android.screen.user.legacy.navigation.UserScreen(
//                com.makentoshe.habrachan.application.android.screen.user.legacy.model.UserAccount.User(
//                    login
//                )
//            )
//        )
    }
}