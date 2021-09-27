package com.makentoshe.habrachan.application.android.screen.articles.navigation

import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.BackwardNavigator
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class BackwardNavigatorImpl @Inject constructor(private val router: Router): BackwardNavigator {
    override fun toPreviousScreen() = router.exit()
}