package com.makentoshe.habrachan.application.android.screen.comments.navigation

import ru.terrakok.cicerone.Router

class ArticleCommentsNavigation(
    private val router: Router,
    private val articleId: Int,
    private val articleTitle: String
) {

    fun back() {
        router.exit()
    }

    fun toDiscussionCommentsFragment(commentId: Int) {
        val screen = DiscussionCommentsScreen(articleId, articleTitle, commentId)
        router.navigateTo(screen)
    }
}