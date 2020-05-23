package com.makentoshe.habrachan.navigation.article

import com.makentoshe.habrachan.common.database.session.SessionDao
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.model.user.UserAccount
import com.makentoshe.habrachan.navigation.comments.CommentsScreen
import com.makentoshe.habrachan.navigation.images.PostImageScreen
import com.makentoshe.habrachan.navigation.user.UserScreen
import ru.terrakok.cicerone.Router

class ArticleFragmentNavigation(private val router: Router, private val sessionDao: SessionDao) {

    /** Returns to MainScreen */
    fun back() {
        router.exit()
    }

    /** Navigates to [PostImageScreen] */
    fun toArticleResourceScreen(resource: String) {
        router.navigateTo(PostImageScreen(resource))
    }

    fun toArticleCommentsScreen(articleId: Int) {
        router.navigateTo(CommentsScreen(articleId))
    }

    fun toArticleCommentsScreen(article: Article) {
        router.navigateTo(CommentsScreen(article))
    }

    fun toUserScreen(userName: String): Boolean {
        if (sessionDao.get().isLoggedIn) {
            router.navigateTo(UserScreen(UserAccount.User(userName)))
            return true
        } else {
            return false
        }
    }
}