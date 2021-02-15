package com.makentoshe.habrachan.application.android.screen.comments.navigation

import com.makentoshe.habrachan.application.android.screen.content.navigation.ContentScreen
import ru.terrakok.cicerone.Router

class CommentsNavigation(
    private val router: Router, private val articleId: Int, private val articleTitle: String
) {

    fun back() {
        router.exit()
    }

    fun toDiscussionCommentsFragment(commentId: Int) {
        val screen = DiscussionCommentsScreen(articleId, articleTitle, commentId)
        router.navigateTo(screen)
    }

    fun toContentScreen(source: String) {
        router.navigateTo(ContentScreen(source))
    }
}