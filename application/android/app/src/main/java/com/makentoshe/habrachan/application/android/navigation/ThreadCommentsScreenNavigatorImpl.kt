package com.makentoshe.habrachan.application.android.navigation

import com.makentoshe.habrachan.application.android.navigation.navigator.ThreadCommentsScreenNavigator
import com.makentoshe.habrachan.application.android.screen.comments.thread.navigation.ThreadCommentsScreen
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.CommentId
import javax.inject.Inject

class ThreadCommentsScreenNavigatorImpl @Inject constructor(
    private val router: StackRouter,
) : ThreadCommentsScreenNavigator {
    override fun toThreadCommentsScreen(articleId: ArticleId, commentId: CommentId) {
        router.stack(ThreadCommentsScreen(articleId.articleId, "test", commentId.commentId))
    }
}
