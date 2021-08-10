package com.makentoshe.habrachan.application.android

import android.content.Intent
import android.net.Uri
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.screen.article.navigation.ArticleScreen
import com.makentoshe.habrachan.application.android.screen.comments.navigation.ArticleCommentsScreen
import com.makentoshe.habrachan.application.android.screen.comments.navigation.DiscussionCommentsScreen
import com.makentoshe.habrachan.application.android.screen.user.model.UserAccount
import com.makentoshe.habrachan.application.android.screen.user.navigation.UserScreen
import com.makentoshe.habrachan.entity.articleId
import ru.terrakok.cicerone.Screen

/**
 * Class resolves startup activity intent and returns a screen to launch first
 *
 * TODO(high): mb hide parsing with kotlin dsl
 * TODO(low): mb replace with parser combinator
 */
class Launcher(private val defaultScreen: Screen) {

    companion object : Analytics(LogAnalytic())

    /** Resolve intent from activity */
    fun launch(intent: Intent) = when (intent.action) {
        Intent.ACTION_MAIN -> default()
        Intent.ACTION_VIEW -> deeplink(intent)
        else -> throw IllegalArgumentException(intent.action)
    }

    /** Returns a default screen that launches if deeplink can't be resolved */
    fun default() = defaultScreen

    /** Try to resolve deeplink intent. If resolve failed default screen launches instead */
    fun deeplink(intent: Intent): Screen {
        capture(analyticEvent { "Deeplinking: ${intent.data}" })
        return deeplink(0, intent.data ?: return default())
    }

    /** Internal deeplink resolving */
    private fun deeplink(depth: Int, uri: Uri): Screen = try {
        when (uri.pathSegments[depth]) {
            "post", "blog" -> article(depth + 1, uri)
            "users", "user" -> users(depth + 1, uri)
            else -> deeplink(depth + 1, uri)
        }
    } catch (ioobe: IndexOutOfBoundsException) {
        defaultScreen
    }

    private fun article(depth: Int, uri: Uri): Screen {
        val articleId = uri.pathSegments[depth].toInt()
        return article(articleId, depth + 1, uri)
    }

    private fun article(articleId: Int, depth: Int, uri: Uri): Screen {
        if (uri.fragment == null) {
            if (uri.pathSegments.size == depth) {
                return articleScreen(articleId)
            }
            if (uri.pathSegments[depth] == "comments") {
                return articleCommentsScreen(articleId)
            }
        }
        val commentId = uri.fragment!!.split('_').last().toInt()
        return articleCommentScreen(articleId, commentId)
    }

    private fun articleCommentsScreen(articleId: Int): ArticleCommentsScreen {
        return ArticleCommentsScreen(articleId(articleId), "Deeplinked...")
    }

    private fun articleCommentScreen(articleId: Int, commentId: Int): DiscussionCommentsScreen {
        return DiscussionCommentsScreen(articleId, "Deeplinked...", commentId)
    }

    private fun articleScreen(articleId: Int): ArticleScreen {
        return ArticleScreen(articleId(articleId))
    }

    private fun users(depth: Int, uri: Uri): Screen {
        val login = uri.pathSegments[depth]
        return UserScreen(UserAccount.User(login))
    }
}