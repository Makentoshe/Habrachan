package com.makentoshe.habrachan.application.android.screen.comments.navigation

import com.makentoshe.habrachan.application.android.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.content.navigation.ContentScreen
import com.makentoshe.habrachan.application.android.screen.user.model.UserAccount
import com.makentoshe.habrachan.application.android.screen.user.navigation.UserScreen
import com.makentoshe.habrachan.entity.User
import ru.terrakok.cicerone.Router

class CommentsNavigation(
    private val router: StackRouter, private val articleId: Int, private val articleTitle: String
) {

    fun back() {
        router.exit()
    }

    fun toDiscussionCommentsFragment(commentId: Int) {
        router.stack(DiscussionCommentsScreen(articleId, articleTitle, commentId))
    }

    fun toContentScreen(source: String) {
        router.stack(ContentScreen(source))
    }

    fun toUserScreen(login: String) {
        router.stack(UserScreen(UserAccount.User(login = login)))
    }

}