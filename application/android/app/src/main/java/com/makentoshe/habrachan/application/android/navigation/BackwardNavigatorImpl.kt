package com.makentoshe.habrachan.application.android.navigation

import com.makentoshe.habrachan.application.android.common.navigation.navigator.BackwardNavigator
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class BackwardNavigatorImpl @Inject constructor(private val router: Router): BackwardNavigator {
    override fun toPreviousScreen() = router.exit()
}