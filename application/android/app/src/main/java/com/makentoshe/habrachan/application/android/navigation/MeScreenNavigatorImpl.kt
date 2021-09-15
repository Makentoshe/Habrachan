package com.makentoshe.habrachan.application.android.navigation

import com.makentoshe.habrachan.application.android.navigation.navigator.MeScreenNavigator
import com.makentoshe.habrachan.application.android.screen.user.model.UserAccount
import com.makentoshe.habrachan.application.android.screen.user.navigation.UserScreen
import javax.inject.Inject

class MeScreenNavigatorImpl @Inject constructor(
    private val router: StackRouter,
) : MeScreenNavigator {
    override fun toMeScreen(login: String?) {
        router.navigateTo(UserScreen(UserAccount.Me(login)))
    }
}

