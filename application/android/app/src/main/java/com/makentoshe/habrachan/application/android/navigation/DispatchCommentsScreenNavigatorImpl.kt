package com.makentoshe.habrachan.application.android.navigation

import com.makentoshe.habrachan.application.android.common.navigation.StackRouter
import com.makentoshe.habrachan.application.android.common.navigation.navigator.DispatchCommentsScreenNavigator
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.navigation.DispatchCommentsScreen
import javax.inject.Inject

class DispatchCommentsScreenNavigatorImpl @Inject constructor(
    private val router: StackRouter
): DispatchCommentsScreenNavigator {
    override fun toDispatchScreen() {
        router.stack(DispatchCommentsScreen())
    }
}