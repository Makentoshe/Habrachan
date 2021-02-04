package com.makentoshe.habrachan.application.android.screen.article.navigation

import com.makentoshe.habrachan.application.android.screen.comments.navigation.ArticleCommentsScreen
import com.makentoshe.habrachan.application.android.screen.content.navigation.ContentScreen
import com.makentoshe.habrachan.entity.Article
import ru.terrakok.cicerone.Router

class ArticleNavigation(private val router: Router) {

    /** Returns to MainScreen */
    fun back() {
        router.exit()
    }

    fun toArticleCommentsScreen(article: Article) {
        router.navigateTo(ArticleCommentsScreen(article))
    }

    fun toArticleContentScreen(source: String) {
        router.navigateTo(ContentScreen(source))
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