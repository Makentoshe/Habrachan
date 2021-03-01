package com.makentoshe.habrachan.application.android.screen.article.navigation

import com.makentoshe.habrachan.application.android.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.comments.navigation.ArticleCommentsScreen
import com.makentoshe.habrachan.application.android.screen.content.navigation.ContentScreen
import com.makentoshe.habrachan.entity.natives.Article

class ArticleNavigation(private val router: StackRouter) {

    /** Returns to MainScreen */
    fun back() {
        router.exit()
    }

    fun toArticleCommentsScreen(article: Article) {
        router.stack(ArticleCommentsScreen(article))
    }

    fun toArticleContentScreen(source: String) {
        router.stack(ContentScreen(source))
    }


//    /** Navigates to [OverlayImageScreen] */
//    fun toArticleResourceScreen(resource: String) {
//        router.navigateTo(OverlayImageScreen(resource))
//    }
//
//    fun toArticleCommentsScreen(article: Article) {
//        router.navigateTo(CommentsScreen(article))
//    }
//
//    fun toUserScreen(userName: String): Boolean {
//        if (sessionDao.get().isLoggedIn) {
//            router.navigateTo(UserScreen(UserAccount.User(userName)))
//            return true
//        } else {
//            return false
//        }
//    }
}