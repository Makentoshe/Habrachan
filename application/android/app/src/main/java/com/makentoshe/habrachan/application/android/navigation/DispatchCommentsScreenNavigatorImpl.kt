package com.makentoshe.habrachan.application.android.navigation

import com.makentoshe.habrachan.application.android.common.navigation.StackRouter
import com.makentoshe.habrachan.application.android.common.navigation.navigator.DispatchCommentsScreenNavigator
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.navigation.DispatchCommentsScreen
import com.makentoshe.habrachan.entity.CommentId
import javax.inject.Inject

class DispatchCommentsScreenNavigatorImpl @Inject constructor(
    private val router: StackRouter
): DispatchCommentsScreenNavigator {
    override fun toDispatchScreen(commentId: CommentId) {
        router.stack(DispatchCommentsScreen(commentId))
    }
}