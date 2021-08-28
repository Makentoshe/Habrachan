package com.makentoshe.habrachan.application.android.navigation

import com.makentoshe.habrachan.application.android.common.navigation.StackRouter
import com.makentoshe.habrachan.application.android.common.navigation.navigator.ContentScreenNavigator
import com.makentoshe.habrachan.application.android.screen.content.navigation.ContentScreen
import javax.inject.Inject

class ContentScreenNavigatorImpl @Inject constructor(private val router: StackRouter) : ContentScreenNavigator {
    override fun toContentScreen(source: String) {
        router.stack(ContentScreen(source))
    }
}