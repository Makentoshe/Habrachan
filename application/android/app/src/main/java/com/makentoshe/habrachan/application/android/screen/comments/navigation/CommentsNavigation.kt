package com.makentoshe.habrachan.application.android.screen.comments.navigation

import com.makentoshe.habrachan.application.android.common.comment.BlockViewControllerNavigator
import com.makentoshe.habrachan.application.android.common.comment.CommentViewControllerNavigator
import com.makentoshe.habrachan.application.android.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.content.navigation.ContentScreen
import com.makentoshe.habrachan.application.android.screen.user.model.UserAccount
import com.makentoshe.habrachan.application.android.screen.user.navigation.UserScreen

class CommentsNavigation(
    private val router: StackRouter, private val articleId: Int, private val articleTitle: String
) : CommentViewControllerNavigator, BlockViewControllerNavigator {

    fun back() {
        router.exit()
    }

    override fun toDiscussionScreen(commentId: Int) {
        router.stack(DiscussionCommentsScreen(articleId, articleTitle, commentId))
    }

    override fun toContentScreen(source: String) {
        router.stack(ContentScreen(source))
    }

    fun toUserScreen(login: String) {
        router.stack(UserScreen(UserAccount.User(login = login)))
    }

}