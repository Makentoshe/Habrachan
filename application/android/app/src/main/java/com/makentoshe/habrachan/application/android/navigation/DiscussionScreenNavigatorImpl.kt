package com.makentoshe.habrachan.application.android.navigation

import com.makentoshe.habrachan.application.android.common.navigation.StackRouter
import com.makentoshe.habrachan.application.android.common.navigation.navigator.DiscussionScreenNavigator
import com.makentoshe.habrachan.application.android.screen.comments.thread.navigation.ThreadCommentsScreen
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.CommentId
import javax.inject.Inject

class DiscussionScreenNavigatorImpl @Inject constructor(private val router: StackRouter) : DiscussionScreenNavigator {
    override fun toDiscussionScreen(articleId: ArticleId, commentId: CommentId) {
        router.stack(ThreadCommentsScreen(articleId.articleId, "test", commentId.commentId))
    }
}
